package fr.eradium.eramod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(TitleScreen.class)
public abstract class MainMenuMixin {
    
    private static final CubeMap CUSTOM_PANORAMA = new CubeMap(ResourceLocation.fromNamespaceAndPath("eramod", "textures/screens/panorama/panorama"));
    private static final PanoramaRenderer CUSTOM_PANORAMA_RENDERER = new PanoramaRenderer(CUSTOM_PANORAMA);
    private static final ResourceLocation CUSTOM_LOGO = ResourceLocation.fromNamespaceAndPath("eramod", "textures/eradium_v4_MagicalArtUpdate.png");

    @Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true)
    private void renderCustomPanorama(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        TitleScreen titleScreen = (TitleScreen)(Object)this;
        CUSTOM_PANORAMA_RENDERER.render(guiGraphics, titleScreen.width, titleScreen.height, 1.0F, partialTick);
        ci.cancel();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LogoRenderer;renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IF)V"))
    private void renderCustomLogo(LogoRenderer logoRenderer, GuiGraphics guiGraphics, int width, float alpha) {
        // Calculate logo position (centered horizontally, positioned like original)
        int logoWidth = 309; // Adjust this based on your logo's actual width
        int logoHeight = 64; // Adjust this based on your logo's actual height
        int x = (width - logoWidth) / 2;
        int y = 20; // Adjust vertical position as needed
        
        // Set the render color with alpha for fade effects
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
        guiGraphics.blit(CUSTOM_LOGO, x, y, 0, 0, logoWidth, logoHeight, logoWidth, logoHeight);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); // Reset color
    }

    // Hide NeoForge branding (version info at bottom left)
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/internal/BrandingControl;forEachLine(ZZLjava/util/function/BiConsumer;)V"))
    private void hideNeoForgeBranding(boolean includeModCount, boolean reverse, java.util.function.BiConsumer<Integer, String> lineConsumer) {
        // Do nothing - this hides the NeoForge version info
    }

    // Hide copyright and additional branding (version info at bottom right)
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/internal/BrandingControl;forEachAboveCopyrightLine(Ljava/util/function/BiConsumer;)V"))
    private void hideAdditionalBranding(java.util.function.BiConsumer<Integer, String> lineConsumer) {
        // Do nothing - this hides additional version/mod info
    }

    // Hide splash text (the yellow text that appears next to the logo)
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/SplashRenderer;render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/client/gui/Font;I)V"))
    private void hideSplash(net.minecraft.client.gui.components.SplashRenderer splashRenderer, GuiGraphics guiGraphics, int width, net.minecraft.client.gui.Font font, int color) {
        // Do nothing - this hides the splash text
    }

}