package fr.eradium.eramod.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.concurrent.ConcurrentHashMap;

public class SkinCacheManager {
    
    private static final ConcurrentHashMap<String, ResourceLocation> customSkinCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> failedSkinCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> checkingCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ResourceLocation> customCapeCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> failedCapeCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> checkingCapeCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> ignoredPlayers = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> ignoredPlayerSkins = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> ignoredPlayerCapes = new ConcurrentHashMap<>();
    
    // Getters for cache maps
    public static ConcurrentHashMap<String, ResourceLocation> getCustomSkinCache() {
        return customSkinCache;
    }
    
    public static ConcurrentHashMap<String, Boolean> getFailedSkinCache() {
        return failedSkinCache;
    }
    
    public static ConcurrentHashMap<String, Boolean> getCheckingCache() {
        return checkingCache;
    }
    
    public static ConcurrentHashMap<String, ResourceLocation> getCustomCapeCache() {
        return customCapeCache;
    }
    
    public static ConcurrentHashMap<String, Boolean> getFailedCapeCache() {
        return failedCapeCache;
    }
    
    public static ConcurrentHashMap<String, Boolean> getCheckingCapeCache() {
        return checkingCapeCache;
    }
    
    public static ConcurrentHashMap<String, Boolean> getIgnoredPlayers() {
        return ignoredPlayers;
    }
    
    /**
     * Adds a player to the ignore list, preventing their custom skins/capes from loading
     * This will cause them to use their vanilla Minecraft skin instead
     */
    public static void ignorePlayer(String playerName) {
        ignorePlayer(playerName, "both");
    }
    
    /**
     * Adds a player to the selective ignore list
     * @param playerName The player to ignore
     * @param type "skin", "cape", or "both"
     */
    public static void ignorePlayer(String playerName, String type) {
        String lowerName = playerName.toLowerCase();
        
        switch (type.toLowerCase()) {
            case "skin":
                ignoredPlayerSkins.put(lowerName, true);
                failedSkinCache.put(playerName, true);
                // Clear skin cache but keep cape cache
                customSkinCache.remove(playerName);
                checkingCache.remove(playerName);
                break;
            case "cape":
                ignoredPlayerCapes.put(lowerName, true);
                failedCapeCache.put(playerName, true);
                // Clear cape cache but keep skin cache
                customCapeCache.remove(playerName);
                checkingCapeCache.remove(playerName);
                break;
            case "both":
            default:
                ignoredPlayers.put(lowerName, true);
                clearPlayerCache(playerName);
                failedSkinCache.put(playerName, true);
                failedCapeCache.put(playerName, true);
                break;
        }
    }
    
    /**
     * Removes a player from the ignore list, allowing their custom skins/capes to load again
     */
    public static void unignorePlayer(String playerName) {
        unignorePlayer(playerName, "both");
    }
    
    /**
     * Removes a player from the selective ignore list
     * @param playerName The player to unignore
     * @param type "skin", "cape", or "both"
     */
    public static void unignorePlayer(String playerName, String type) {
        String lowerName = playerName.toLowerCase();
        
        switch (type.toLowerCase()) {
            case "skin":
                ignoredPlayerSkins.remove(lowerName);
                failedSkinCache.remove(playerName);
                // Clear skin cache to allow fresh checks
                customSkinCache.remove(playerName);
                checkingCache.remove(playerName);
                break;
            case "cape":
                ignoredPlayerCapes.remove(lowerName);
                failedCapeCache.remove(playerName);
                // Clear cape cache to allow fresh checks
                customCapeCache.remove(playerName);
                checkingCapeCache.remove(playerName);
                break;
            case "both":
            default:
                ignoredPlayers.remove(lowerName);
                ignoredPlayerSkins.remove(lowerName);
                ignoredPlayerCapes.remove(lowerName);
                clearPlayerCache(playerName);
                failedSkinCache.remove(playerName);
                failedCapeCache.remove(playerName);
                break;
        }
    }
    
    /**
     * Checks if a player is ignored (both skin and cape)
     */
    public static boolean isPlayerIgnored(String playerName) {
        return ignoredPlayers.containsKey(playerName.toLowerCase());
    }
    
    /**
     * Checks if a player's skin is ignored
     */
    public static boolean isPlayerSkinIgnored(String playerName) {
        String lowerName = playerName.toLowerCase();
        return ignoredPlayers.containsKey(lowerName) || ignoredPlayerSkins.containsKey(lowerName);
    }
    
    /**
     * Checks if a player's cape is ignored
     */
    public static boolean isPlayerCapeIgnored(String playerName) {
        String lowerName = playerName.toLowerCase();
        return ignoredPlayers.containsKey(lowerName) || ignoredPlayerCapes.containsKey(lowerName);
    }
    
    /**
     * Clears all cached data for a specific player, forcing a reload on next skin request
     */
    public static void clearPlayerCache(String playerName) {
        customSkinCache.remove(playerName);
        failedSkinCache.remove(playerName);
        checkingCache.remove(playerName);
        customCapeCache.remove(playerName);
        failedCapeCache.remove(playerName);
        checkingCapeCache.remove(playerName);
        
        // Also remove textures from the texture manager if they exist
        ResourceLocation skinTexture = ResourceLocation.fromNamespaceAndPath("eramod", "dynamic/skin/" + playerName.toLowerCase());
        ResourceLocation capeTexture = ResourceLocation.fromNamespaceAndPath("eramod", "dynamic/cape/" + playerName.toLowerCase());
        
        if (FMLEnvironment.dist.isClient()) {
            Minecraft.getInstance().execute(() -> {
                TextureManager textureManager = Minecraft.getInstance().getTextureManager();
                textureManager.release(skinTexture);
                textureManager.release(capeTexture);
            });
        }
    }
    
    /**
     * Clears all cached data for all players, forcing a complete reload
     */
    public static void clearAllCache() {
        if (FMLEnvironment.dist.isClient()) {
            Minecraft.getInstance().execute(() -> {
                TextureManager textureManager = Minecraft.getInstance().getTextureManager();
                
                // Release all cached textures from texture manager
                for (String playerName : customSkinCache.keySet()) {
                    ResourceLocation skinTexture = ResourceLocation.fromNamespaceAndPath("eramod", "dynamic/skin/" + playerName.toLowerCase());
                    textureManager.release(skinTexture);
                }
                
                for (String playerName : customCapeCache.keySet()) {
                    ResourceLocation capeTexture = ResourceLocation.fromNamespaceAndPath("eramod", "dynamic/cape/" + playerName.toLowerCase());
                    textureManager.release(capeTexture);
                }
            });
        }
        
        // Clear all cache maps
        customSkinCache.clear();
        failedSkinCache.clear();
        checkingCache.clear();
        customCapeCache.clear();
        failedCapeCache.clear();
        checkingCapeCache.clear();
    }
}