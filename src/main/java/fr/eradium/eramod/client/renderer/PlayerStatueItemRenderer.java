package fr.eradium.eramod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class PlayerStatueItemRenderer extends BlockEntityWithoutLevelRenderer {
	private static final Set<ResourceLocation> LOADED_TEXTURES = new HashSet<>();
	private final PlayerModel<?> playerModel;
	private final PlayerModel<?> playerModelSlim;
	private static final ResourceLocation STEVE_SKIN = ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");

	public PlayerStatueItemRenderer(BlockEntityRenderDispatcher dispatcher, Minecraft minecraft) {
		super(dispatcher, minecraft.getEntityModels());
		this.playerModel = new PlayerModel<>(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
		this.playerModelSlim = new PlayerModel<>(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), true);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		poseStack.pushPose();

		// Get player data from NBT
		ResourceLocation skinTexture = STEVE_SKIN;
		PlayerModel<?> model = playerModel;
		UUID playerUUID = null;
		
		if (stack.has(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA)) {
			var blockEntityData = stack.get(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA);
			if (blockEntityData != null) {
				var nbt = blockEntityData.copyTag();
				
				// Get UUID if available
				if (nbt.hasUUID("PlayerUUID")) {
					playerUUID = nbt.getUUID("PlayerUUID");
				}
				
				// Get skin texture if available
				if (nbt.contains("SkinTextureBase64") && playerUUID != null) {
					String textureBase64 = nbt.getString("SkinTextureBase64");
					skinTexture = getDynamicSkinLocation(playerUUID, textureBase64);
				}
				
				// Get model type if available
				if (nbt.contains("SkinModel")) {
					String skinModel = nbt.getString("SkinModel");
					if ("slim".equals(skinModel)) {
						model = playerModelSlim;
					}
				}
			}
		}

		// Adjust based on display context
		switch (displayContext) {
			case GUI -> {
				// Inventory/GUI rendering
				poseStack.translate(0.5, 0.70, 0.5);
				poseStack.scale(0.45f, 0.45f, 0.45f);
				poseStack.mulPose(Axis.XP.rotationDegrees(180));
				poseStack.mulPose(Axis.YP.rotationDegrees(45));
			}
			case GROUND -> {
				// Dropped item rendering
				poseStack.translate(0.5, 0.75, 0.5);
				poseStack.scale(0.3f, 0.3f, 0.3f);
				poseStack.mulPose(Axis.XP.rotationDegrees(180));
			}
			case FIXED -> {
				// Item frame rendering
				poseStack.translate(0.5, 0.75, 0.5);
				poseStack.scale(0.4f, 0.4f, 0.4f);
				poseStack.mulPose(Axis.XP.rotationDegrees(180));
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
			}
			case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> {
				// Third person hand rendering
				poseStack.translate(0.5, 0.75, 0.5);
				poseStack.scale(0.25f, 0.25f, 0.25f);
				poseStack.mulPose(Axis.XP.rotationDegrees(180));
			}
			case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> {
				// First person hand rendering
				poseStack.translate(0.5, 0.75, 0.5);
				poseStack.scale(0.3f, 0.3f, 0.3f);
				poseStack.mulPose(Axis.XP.rotationDegrees(180));
			}
			default -> {
				// Default rendering
				poseStack.translate(0.5, 0.75, 0.5);
				poseStack.scale(0.5f, 0.5f, 0.5f);
				poseStack.mulPose(Axis.XP.rotationDegrees(180));
			}
		}

		// Setup the model pose
		model.young = false;
		model.setAllVisible(true);
		
		// Check if this is an old 64x32 skin format and hide overlay layers if needed
		setupModelLayers(model, skinTexture);
		
		// Reset all rotations to standing pose
		model.head.xRot = 0;
		model.head.yRot = 0;
		model.head.zRot = 0;
		
		model.body.xRot = 0;
		model.body.yRot = 0;
		model.body.zRot = 0;
		
		model.rightArm.xRot = 0;
		model.rightArm.yRot = 0;
		model.rightArm.zRot = 0;
		
		model.leftArm.xRot = 0;
		model.leftArm.yRot = 0;
		model.leftArm.zRot = 0;
		
		model.rightLeg.xRot = 0;
		model.rightLeg.yRot = 0;
		model.rightLeg.zRot = 0;
		
		model.leftLeg.xRot = 0;
		model.leftLeg.yRot = 0;
		model.leftLeg.zRot = 0;

		// Render the model with the selected skin
		var vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(skinTexture));
		model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);

		poseStack.popPose();
	}
	
	private ResourceLocation getDynamicSkinLocation(UUID playerUUID, String textureBase64) {
		// Create a unique resource location for this cached skin
		ResourceLocation location = ResourceLocation.fromNamespaceAndPath("eramod", "skins/item_" + playerUUID.toString());
		
		// Check if we've already loaded this texture
		if (LOADED_TEXTURES.contains(location)) {
			return location;
		}
		
		// Try to get the texture manager and upload the texture
		try {
			Minecraft minecraft = Minecraft.getInstance();
			var textureManager = minecraft.getTextureManager();
			
			// Decode base64 and create texture
			byte[] imageBytes = java.util.Base64.getDecoder().decode(textureBase64);
			java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(imageBytes);
			
			// Load the image
			var image = com.mojang.blaze3d.platform.NativeImage.read(inputStream);
			
			// Register the texture
			textureManager.register(location, new net.minecraft.client.renderer.texture.DynamicTexture(image));
			
			// Mark as loaded
			LOADED_TEXTURES.add(location);
			
			return location;
		} catch (Exception e) {
			// If loading fails, fallback to Steve skin
			return STEVE_SKIN;
		}
	}
	
	private void setupModelLayers(PlayerModel<?> model, ResourceLocation skinLocation) {
		// Try to detect if this is a 64x32 (old) or 64x64 (new) skin
		boolean isOldSkin = isOldSkinFormat(skinLocation);
		
		// For old 64x32 skins, hide the overlay layers (they don't exist in old format)
		if (isOldSkin) {
			model.hat.visible = false;
			model.jacket.visible = false;
			model.leftSleeve.visible = false;
			model.rightSleeve.visible = false;
			model.leftPants.visible = false;
			model.rightPants.visible = false;
		} else {
			// For new 64x64 skins, show all layers
			model.hat.visible = true;
			model.jacket.visible = true;
			model.leftSleeve.visible = true;
			model.rightSleeve.visible = true;
			model.leftPants.visible = true;
			model.rightPants.visible = true;
		}
	}
	
	private boolean isOldSkinFormat(ResourceLocation skinLocation) {
		// Try to get the texture and check its dimensions
		try {
			Minecraft minecraft = Minecraft.getInstance();
			var textureManager = minecraft.getTextureManager();
			var texture = textureManager.getTexture(skinLocation);
			
			if (texture instanceof net.minecraft.client.renderer.texture.DynamicTexture dynamicTexture) {
				var pixels = dynamicTexture.getPixels();
				if (pixels != null) {
					int height = pixels.getHeight();
					// Old skins are 64x32, new skins are 64x64
					return height == 32;
				}
			}
		} catch (Exception e) {
			// If we can't determine, assume new format (safer default)
		}
		
		// Default to new format if we can't determine
		return false;
	}
}
