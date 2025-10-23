package fr.eradium.eramod.client.renderer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import fr.eradium.eramod.EramodMod;
import fr.eradium.eramod.block.PlayerStatueBlock;
import fr.eradium.eramod.block.entity.PlayerStatueBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlayerStatueRenderer implements BlockEntityRenderer<PlayerStatueBlockEntity> {
	private static final Set<ResourceLocation> LOADED_TEXTURES = new HashSet<>();
	
	private final PlayerModel<?> playerModel;
	private final PlayerModel<?> playerModelSlim;

	public PlayerStatueRenderer(BlockEntityRendererProvider.Context context) {
		this.playerModel = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false);
		this.playerModelSlim = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
	}

	@Override
	public void render(PlayerStatueBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		UUID playerUUID = blockEntity.getPlayerUUID();
		// Always render, even if playerUUID is null (will show Steve skin)
		
		poseStack.pushPose();

		// Get the facing direction and rotate accordingly
		Direction facing = blockEntity.getBlockState().getValue(PlayerStatueBlock.FACING);
		poseStack.translate(0.5, 0.0, 0.5);
		
		float rotation = switch (facing) {
			case NORTH -> 180f;
			case SOUTH -> 0f;
			case WEST -> -90f;
			case EAST -> 90f;
			default -> 0f;
		};
		poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

		// Scale and position the model (adjust as needed)
		poseStack.scale(0.5f, 0.5f, 0.5f);
		poseStack.translate(0, 1.5, 0); // Move up and flip

		// Flip the model right-side up
		poseStack.mulPose(Axis.XP.rotationDegrees(180));

		// Get player skin texture
		ResourceLocation skinLocation = getSkinLocation(playerUUID, blockEntity);
		
		// Determine model type (slim or normal)
		PlayerModel<?> model = getPlayerModel(playerUUID, blockEntity);
		
		// Setup the model pose (standing pose)
		model.young = false;
		model.setAllVisible(true);
		
		// Check if this is an old 64x32 skin format and hide overlay layers if needed
		setupModelLayers(model, skinLocation);
		
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

		// Render the model
		VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(skinLocation));
		model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

		poseStack.popPose();
	}

	private ResourceLocation getSkinLocation(UUID playerUUID, PlayerStatueBlockEntity blockEntity) {
		// If playerUUID is null, always show Steve skin
		if (playerUUID == null) {
			return ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
		}
		
		// First check if we have cached texture data
		String cachedTexture = blockEntity.getSkinTextureBase64();
		if (cachedTexture != null && !cachedTexture.isEmpty()) {
			// Use cached texture - we'll need to register it dynamically
			return getDynamicSkinLocation(playerUUID, cachedTexture);
		}
		
		// If no cached texture, default to Steve skin while async fetch is in progress
		// This prevents the statue from being invisible during API requests
		return ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
	}

	private ResourceLocation getDynamicSkinLocation(UUID playerUUID, String textureBase64) {
		// Create a unique resource location for this cached skin
		ResourceLocation location = ResourceLocation.fromNamespaceAndPath("eramod", "skins/statue_" + playerUUID.toString());
		
		// Check if we've already loaded this texture
		if (LOADED_TEXTURES.contains(location)) {
			return location;
		}
		
		// Try to get the texture manager and upload the texture
		try {
			EramodMod.LOGGER.info("[PlayerStatue Renderer] Loading dynamic texture for: {}", playerUUID);
			EramodMod.LOGGER.info("[PlayerStatue Renderer] Base64 length: {}", textureBase64.length());
			
			Minecraft minecraft = Minecraft.getInstance();
			var textureManager = minecraft.getTextureManager();
			
			// Decode base64 and create texture
			byte[] imageBytes = java.util.Base64.getDecoder().decode(textureBase64);
			EramodMod.LOGGER.info("[PlayerStatue Renderer] Decoded bytes length: {}", imageBytes.length);
			
			java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(imageBytes);
			
			// Load the image
			var image = com.mojang.blaze3d.platform.NativeImage.read(inputStream);
			EramodMod.LOGGER.info("[PlayerStatue Renderer] Image loaded: {}x{}", image.getWidth(), image.getHeight());
			
			// Register the texture
			textureManager.register(location, new net.minecraft.client.renderer.texture.DynamicTexture(image));
			EramodMod.LOGGER.info("[PlayerStatue Renderer] Texture registered successfully!");
			
			// Mark as loaded
			LOADED_TEXTURES.add(location);
			
			return location;
		} catch (Exception e) {
			EramodMod.LOGGER.error("[PlayerStatue Renderer] Error loading dynamic texture: {}", e.getMessage(), e);
			// If loading fails, fallback to Steve skin
			return ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
		}
	}

	private PlayerModel<?> getPlayerModel(UUID playerUUID, PlayerStatueBlockEntity blockEntity) {
		// If playerUUID is null, return default Steve model
		if (playerUUID == null) {
			return playerModel;
		}
		
		// First check if we have cached model data
		String cachedModel = blockEntity.getSkinModel();
		if (cachedModel != null && !cachedModel.isEmpty() && !"default".equals(cachedModel)) {
			return "slim".equals(cachedModel) ? playerModelSlim : playerModel;
		}
		
		Minecraft minecraft = Minecraft.getInstance();
		
		// Try to get the player model type
		if (minecraft.level != null) {
			var player = minecraft.level.getPlayerByUUID(playerUUID);
			if (player != null) {
				String modelName = minecraft.getSkinManager().getInsecureSkin(player.getGameProfile()).model().id();
				return "slim".equals(modelName) ? playerModelSlim : playerModel;
			}
		}
		
		// If player is not in the level, try to get from connection
		if (minecraft.getConnection() != null) {
			var playerInfo = minecraft.getConnection().getPlayerInfo(playerUUID);
			if (playerInfo != null) {
				String modelName = playerInfo.getSkin().model().id();
				return "slim".equals(modelName) ? playerModelSlim : playerModel;
			}
		}
		
		// Fallback to classic Steve model
		return playerModel;
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
			EramodMod.LOGGER.debug("[PlayerStatue Renderer] Could not determine skin format, assuming 64x64");
		}
		
		// Default to new format if we can't determine
		return false;
	}
}
