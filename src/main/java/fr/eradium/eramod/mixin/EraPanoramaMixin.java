package fr.eradium.eramod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(Screen.class)
public abstract class EraPanoramaMixin {
	
	private static final CubeMap CUSTOM_PANORAMA = new CubeMap(ResourceLocation.fromNamespaceAndPath("eramod", "textures/screens/panorama/panorama"));
	private static final PanoramaRenderer CUSTOM_PANORAMA_RENDERER = new PanoramaRenderer(CUSTOM_PANORAMA);
	
	@Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true, require = 0)
	private void renderCustomPanorama(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
		Screen screen = (Screen)(Object)this;
		CUSTOM_PANORAMA_RENDERER.render(guiGraphics, screen.width, screen.height, 1.0F, partialTick);
		ci.cancel();
	}
}