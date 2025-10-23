package fr.eradium.eramod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.eradium.eramod.client.gui.CreditsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(TitleScreen.class)
public abstract class MainMenuMixin extends Screen {

    private static final CubeMap CUSTOM_PANORAMA = new CubeMap(ResourceLocation.fromNamespaceAndPath("eramod", "textures/screens/panorama/panorama"));
	private static final PanoramaRenderer CUSTOM_PANORAMA_RENDERER = new PanoramaRenderer(CUSTOM_PANORAMA);
    
    private static final ResourceLocation CUSTOM_LOGO = ResourceLocation.fromNamespaceAndPath("eramod", "textures/eradium_v4_MagicalArtUpdate.png");

    // Fields to track the clickable credits text bounds
    private int creditsTextX = 0;
    private int creditsTextY = 0;
    private int creditsTextWidth = 0;
    private int creditsTextHeight = 0;
    private boolean hasCreditsText = false;

    protected MainMenuMixin(Component title) {
        super(title);
    }

	@Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true, require = 0)
	private void renderCustomPanorama(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
		Screen screen = (Screen)(Object)this;
		CUSTOM_PANORAMA_RENDERER.render(guiGraphics, screen.width, screen.height, 1.0F, partialTick);
		ci.cancel();
	}

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LogoRenderer;renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IF)V"), require = 0)
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
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/internal/BrandingControl;forEachLine(ZZLjava/util/function/BiConsumer;)V"), require = 0)
    private void hideNeoForgeBranding(boolean includeModCount, boolean reverse, java.util.function.BiConsumer<Integer, String> lineConsumer) {
        // Show custom clickable credits message
        lineConsumer.accept(0, "Made possible with our amazing partners");
    }

    // Inject after render to draw the clickable credits text and capture its bounds
    @Inject(method = "render", at = @At("TAIL"))
    private void renderClickableCredits(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        // The branding text appears at bottom-left
        String creditsText = "Made possible with our amazing partners";
        int textX = 2;
        int textY = this.height - 10;
        
        // Store bounds for click detection
        this.creditsTextX = textX;
        this.creditsTextY = textY;
        this.creditsTextWidth = this.font.width(creditsText);
        this.creditsTextHeight = 9; // Standard font height
        this.hasCreditsText = true;
        
        // Check if mouse is hovering over the text
        boolean isHovering = mouseX >= creditsTextX && mouseX <= creditsTextX + creditsTextWidth &&
                            mouseY >= creditsTextY && mouseY <= creditsTextY + creditsTextHeight;
        
        // Draw with underline if hovering
        if (isHovering) {
            guiGraphics.drawString(this.font, creditsText, textX, textY, 0xFFFFFF, false); // White when hovering
            // Underline it
            guiGraphics.drawString(this.font, "\u00A7n" + creditsText, textX, textY, 0xFFFFFF, false);
        }
    }

    // Handle mouse clicks on the credits text
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.hasCreditsText && button == 0) { // Left click
            if (mouseX >= creditsTextX && mouseX <= creditsTextX + creditsTextWidth &&
                mouseY >= creditsTextY && mouseY <= creditsTextY + creditsTextHeight) {
                // Open credits screen
                if (this.minecraft != null) {
                    this.minecraft.setScreen(new CreditsScreen((TitleScreen)(Object)this));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // Hide copyright and additional branding (version info at bottom right)
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/internal/BrandingControl;forEachAboveCopyrightLine(Ljava/util/function/BiConsumer;)V"), require = 0)
    private void hideAdditionalBranding(java.util.function.BiConsumer<Integer, String> lineConsumer) {
        // Do nothing - this hides additional version/mod info
    }

    // Hide splash text (the yellow text that appears next to the logo)
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/SplashRenderer;render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/client/gui/Font;I)V"), require = 0)
    private void hideSplash(net.minecraft.client.gui.components.SplashRenderer splashRenderer, GuiGraphics guiGraphics, int width, net.minecraft.client.gui.Font font, int color) {
        // Do nothing - this hides the splash text
    }


}