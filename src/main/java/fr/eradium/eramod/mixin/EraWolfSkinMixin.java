package fr.eradium.eramod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

@Mixin(WolfRenderer.class)
public abstract class EraWolfSkinMixin {
    
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Wolf;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void getTextureLocation(Wolf wolf, CallbackInfoReturnable<ResourceLocation> cir) {
        if (wolf.hasCustomName() && wolf.getCustomName() != null) {
            String customName = wolf.getCustomName().getString();
            
            if ("Griffy".equals(customName)) {
                cir.setReturnValue(ResourceLocation.fromNamespaceAndPath("eramod", "textures/entities/griffy.png"));
            }

            if ("Loup Bleu".equals(customName)) {

                if (wolf.isAngry()) {
                    cir.setReturnValue(ResourceLocation.fromNamespaceAndPath("eramod", "textures/entities/lb_angry.png"));
                } else {
                    if (!wolf.isTame()) {
                        cir.setReturnValue(ResourceLocation.fromNamespaceAndPath("eramod", "textures/entities/lb.png"));
                    } else {
                        cir.setReturnValue(ResourceLocation.fromNamespaceAndPath("eramod", "textures/entities/lb_tame.png"));
                    }
                }
            }

            if ("Houdini".equals(customName)) {
               if (wolf.isAngry()) {
                    cir.setReturnValue(ResourceLocation.fromNamespaceAndPath("eramod", "textures/entities/houdini_angry.png"));
                } else {
                    cir.setReturnValue(ResourceLocation.fromNamespaceAndPath("eramod", "textures/entities/houdini.png"));
                }
            }
        }
    }
}