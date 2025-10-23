package fr.eradium.eramod.client.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreditsScreen extends Screen {
	private final Screen lastScreen;
	
	// Logos (adjust paths to your actual logo locations)
	private static final ResourceLocation LOGO_1 = ResourceLocation.fromNamespaceAndPath("eramod", "textures/gui/credits/logo1.png");
	private static final ResourceLocation LOGO_2 = ResourceLocation.fromNamespaceAndPath("eramod", "textures/gui/credits/logo2.png");
	
	public CreditsScreen(Screen lastScreen) {
		super(Component.literal("Credits"));
		this.lastScreen = lastScreen;
	}
	
	@Override
	protected void init() {
		super.init();
		
		// Add back button at the bottom center
		this.addRenderableWidget(Button.builder(Component.literal("Back"), button -> {
			if (this.minecraft != null) {
				this.minecraft.setScreen(this.lastScreen);
			}
		}).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build());
	}
	
	@Override
	public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		// Render panorama background (same as main menu)
		this.renderPanorama(guiGraphics, partialTick);
		
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		
		int centerX = this.width / 2;
		int startY = 30; // Moved up from 50
		
		// Title
		guiGraphics.drawCenteredString(this.font, "Credits", centerX, startY, 0xFFFFFF);
		
		// Calculate positions for side-by-side layout
		int spacing = 215; // Distance between the two sections
		int leftCenterX = centerX - spacing / 2;
		int rightCenterX = centerX + spacing / 2;
		int sectionsY = startY + 30; // Moved up from 40
		
		// First credit section (LEFT)
		// Logo 1 (adjust size as needed)
		try {
			int logo1Width = 200;
			int logo1Height = 36;
			// Use float UV coordinates for proper texture mapping
			guiGraphics.blit(LOGO_1, leftCenterX - logo1Width / 2, sectionsY + 25, 0.0F, 0.0F, logo1Width, logo1Height, logo1Width, logo1Height);
		} catch (Exception e) {
			// If logo fails to load, just show text
		}
		
		// Name 1
		guiGraphics.drawCenteredString(this.font, "Frost World Studio", leftCenterX, sectionsY + 70, 0xFFFFFF);
		
		// Description 1 (multi-line)
		String[] desc1Lines = {
			"Our game development studio",
			"We are passionate about",
            "creating immersive experiences",
			"We made this mod possible"
		};
		int desc1Y = sectionsY + 85;
		for (String line : desc1Lines) {
			guiGraphics.drawCenteredString(this.font, line, leftCenterX, desc1Y, 0xAAAAAA);
			desc1Y += 12;
		}
		
		// Second credit section (RIGHT)
		// Logo 2
		try {
			int logo2Width = 200;
			int logo2Height = 26;
			// Use blitSprite or RenderSystem to ensure proper alpha blending
			RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            guiGraphics.blit(LOGO_2, rightCenterX - logo2Width / 2, sectionsY + 25, 0.0F, 0.0F, logo2Width, logo2Height, logo2Width, logo2Height);
            RenderSystem.disableBlend();
		} catch (Exception e) {
			// If logo fails to load, just show text
		}
		
		// Name 2
		guiGraphics.drawCenteredString(this.font, "Antasia Cloud", rightCenterX, sectionsY + 70, 0xFFFFFF);
		
		// Description 2 (multi-line)
		String[] desc2Lines = {
			"They provide reliable cloud hosting",
			"They are the one hosting EradiumV4",
			"Thanks to them, we stay online",
            "and protected from threats"
		};
		int desc2Y = sectionsY + 85;
		for (String line : desc2Lines) {
			guiGraphics.drawCenteredString(this.font, line, rightCenterX, desc2Y, 0xAAAAAA);
			desc2Y += 12;
		}
	}
}
