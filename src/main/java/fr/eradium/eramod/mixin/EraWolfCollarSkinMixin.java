package fr.eradium.eramod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

@Mixin(WolfCollarLayer.class)
public abstract class EraWolfCollarSkinMixin {
    
    @Overwrite
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, Wolf wolf, 
                      float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (wolf.isTame() && !wolf.isInvisible()) {
            ResourceLocation collarTexture;
            int collarColor;
            
            // Check if this is Griffy
            /*if (wolf.hasCustomName() && wolf.getCustomName() != null && "Griffy".equals(wolf.getCustomName().getString())) {
                collarTexture = ResourceLocation.fromNamespaceAndPath("eramod", "textures/entities/griffy_collar.png");
            } else {
                collarTexture = ResourceLocation.withDefaultNamespace("textures/entity/wolf/wolf_collar.png");
            }*/

            String customName = wolf.getCustomName() != null ? wolf.getCustomName().getString() : "";
            switch (customName) {
                case "Griffy":
                    collarTexture = ResourceLocation.fromNamespaceAndPath("eramod", "textures/entities/griffy_collar.png");
                    collarColor = 0xFFFFFF; // White collar for Griffy
                    break;
                case "Houdini":
                    collarTexture = ResourceLocation.fromNamespaceAndPath("eramod", "textures/entities/houdini_collar.png");
                    collarColor = 0xFFFFFF; // White collar for Houdini
                    break;
                default:
                    collarTexture = ResourceLocation.withDefaultNamespace("textures/entity/wolf/wolf_collar.png");
                    collarColor = wolf.getCollarColor().getTextureDiffuseColor();
                    break;
            }
            
            // color should be forced to white for griffy collar
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(collarTexture));
            ((WolfModel<?>) ((WolfCollarLayer) (Object) this).getParentModel()).renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, collarColor);
        }
    }
}