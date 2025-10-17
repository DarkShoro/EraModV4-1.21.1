package fr.eradium.eramod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

import fr.eradium.eramod.EramodMod;
import fr.eradium.eramod.util.SkinCacheManager;

@Mixin(AbstractClientPlayer.class)
@OnlyIn(Dist.CLIENT)
public abstract class EraSkinAPIMixin {
    
    private static final String SKIN_API_BASE_URL = "https://eradium.fr/api/skin-api/skins/";
    private static final String CAPE_API_BASE_URL = "https://eradium.fr/api/skin-api/capes/";
    
    @Inject(method = "getSkin()Lnet/minecraft/client/resources/PlayerSkin;", at = @At("RETURN"), cancellable = true)
    public void getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        // Early return if not on client side
        if (!FMLEnvironment.dist.isClient()) {
            return;
        }
        
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
        String playerName = player.getGameProfile().getName();
        PlayerSkin originalSkin = cir.getReturnValue();
        
        // Check if player is ignored - if so, don't apply custom skins/capes
        // Return early to preserve their vanilla Minecraft skin
        if (SkinCacheManager.isPlayerIgnored(playerName)) {
            // Ensure we don't accidentally cache anything for ignored players
            cir.setReturnValue(originalSkin);
            return;
        }
        
        ResourceLocation customSkinTexture = null;
        ResourceLocation customCapeTexture = null;
        boolean shouldModify = false;
        
        // Check for custom skin (only if not ignored)
        if (!SkinCacheManager.isPlayerSkinIgnored(playerName)) {
            if (SkinCacheManager.getCustomSkinCache().containsKey(playerName)) {
                customSkinTexture = SkinCacheManager.getCustomSkinCache().get(playerName);
                if (customSkinTexture != null) {
                    shouldModify = true;
                }
            } else if (!SkinCacheManager.getFailedSkinCache().containsKey(playerName) && !SkinCacheManager.getCheckingCache().containsKey(playerName)) {
                SkinCacheManager.getCheckingCache().put(playerName, true);
                // Check skin API asynchronously
                checkAPI(playerName, SKIN_API_BASE_URL, SkinCacheManager.getCustomSkinCache(), SkinCacheManager.getFailedSkinCache(), SkinCacheManager.getCheckingCache(), "skin");
            }
        }
        
        // Check for custom cape (only if not ignored)
        if (!SkinCacheManager.isPlayerCapeIgnored(playerName)) {
            if (SkinCacheManager.getCustomCapeCache().containsKey(playerName)) {
                customCapeTexture = SkinCacheManager.getCustomCapeCache().get(playerName);
                if (customCapeTexture != null) {
                    shouldModify = true;
                }
            } else if (!SkinCacheManager.getFailedCapeCache().containsKey(playerName) && !SkinCacheManager.getCheckingCapeCache().containsKey(playerName)) {
                SkinCacheManager.getCheckingCapeCache().put(playerName, true);
                // Check cape API asynchronously
                checkAPI(playerName, CAPE_API_BASE_URL, SkinCacheManager.getCustomCapeCache(), SkinCacheManager.getFailedCapeCache(), SkinCacheManager.getCheckingCapeCache(), "cape");
            }
        }
        
        // If we have custom textures, create a new PlayerSkin
        if (shouldModify) {
            ResourceLocation skinTexture = customSkinTexture != null ? customSkinTexture : originalSkin.texture();
            ResourceLocation capeTexture = customCapeTexture != null ? customCapeTexture : originalSkin.capeTexture();
            
            PlayerSkin customPlayerSkin = new PlayerSkin(
                skinTexture,
                originalSkin.textureUrl(),
                capeTexture,
                originalSkin.elytraTexture(),
                originalSkin.model(),
                originalSkin.secure()
            );
            
            cir.setReturnValue(customPlayerSkin);
        }
    }
    
    private void checkAPI(String playerName, String apiBaseUrl, ConcurrentHashMap<String, ResourceLocation> cache, 
                         ConcurrentHashMap<String, Boolean> failedCache, ConcurrentHashMap<String, Boolean> checkingCache, 
                         String type) {
        CompletableFuture.runAsync(() -> {
            try {
                String apiUrl = apiBaseUrl + playerName;
                URI uri = new URI(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
                connection.setRequestMethod("GET"); // Use GET to download the image
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);
                
                int responseCode = connection.getResponseCode();
                
                if (responseCode == 200) {
                    // Download and register the texture
                    try (InputStream inputStream = connection.getInputStream()) {
                        NativeImage nativeImage = NativeImage.read(inputStream);
                        if (nativeImage != null) {
                            // Create a unique ResourceLocation for this custom texture
                            ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath("eramod", "dynamic/" + type + "/" + playerName.toLowerCase());
                            
                            // Register the texture with Minecraft's TextureManager on the main thread
                            Minecraft.getInstance().execute(() -> {
                                try {
                                    TextureManager textureManager = Minecraft.getInstance().getTextureManager();
                                    DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);
                                    textureManager.register(textureLocation, dynamicTexture);
                                    
                                    // Cache the successfully registered texture
                                    cache.put(playerName, textureLocation);
                                    //System.out.println("Custom " + type + " downloaded and registered for player: " + playerName);
                                    EramodMod.LOGGER.info("Custom {} downloaded and registered for player: {}", type, playerName);
                                } catch (Exception e) {
                                    EramodMod.LOGGER.error("Failed to register texture for {}: {}", playerName, e.getMessage());
                                    failedCache.put(playerName, true);
                                }
                            });
                        } else {
                            // System.err.println("Failed to read image data for " + playerName + " " + type);
                            EramodMod.LOGGER.error("Failed to read image data for {} {}", playerName, type);
                            failedCache.put(playerName, true);
                        }
                    }
                } else if (responseCode == 404) {
                    // API returned 404, mark as failed
                    failedCache.put(playerName, true);
                    EramodMod.LOGGER.info("No custom {} found for player: {} (404)", type, playerName);
                } else {
                    EramodMod.LOGGER.error("Unexpected response code {} for {} API request: {}", responseCode, type, apiUrl);
                }
                
                connection.disconnect();
                
            } catch (IOException | URISyntaxException e) {
                // Mark as failed to prevent spam retries on network errors
                failedCache.put(playerName, true);
                EramodMod.LOGGER.warn("Failed to check custom {} for {} (marked as failed to prevent retry spam): {}", type, playerName, e.getMessage());
            } finally {
                checkingCache.remove(playerName);
            }
        });
    }
    

}