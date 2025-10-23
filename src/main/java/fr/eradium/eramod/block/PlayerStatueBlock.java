package fr.eradium.eramod.block;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.eradium.eramod.EramodMod;
import fr.eradium.eramod.block.entity.PlayerStatueBlockEntity;
import fr.eradium.eramod.client.cache.PlayerSkinCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlayerStatueBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	
	// Player-specific particle effects mapping
	private static final java.util.Map<String, net.minecraft.core.particles.SimpleParticleType> PLAYER_PARTICLES = java.util.Map.of(
		"Matcha_Green_Fox", ParticleTypes.ENCHANT,
		"Kazumi_Avali", ParticleTypes.ENCHANT,
		"Topaz_Novabeast", ParticleTypes.ENCHANT,
		"Chibigen", ParticleTypes.ENCHANT,

		"LightShoro", ParticleTypes.END_ROD,
		"DarkShoro", ParticleTypes.SMOKE,
		"PlaSmaShoro", ParticleTypes.HAPPY_VILLAGER,

		"MisterNox_", ParticleTypes.END_ROD,

		"Deforstarb", ParticleTypes.CRIT,
		"Sakuya_Senpai", ParticleTypes.CHERRY_LEAVES
	);
	
	// Special person who gets hearts particles, reserved only to one person, no list, of course.
	// It wouldn't make sense to have multiple people here.
	private static final String LOVE = "Idraano";

	public PlayerStatueBlock() {
		super(BlockBehaviour.Properties.of().strength(1f, 10f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false).instrument(NoteBlockInstrument.BASEDRUM));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			default -> Shapes.or(box(6, 6, 7, 10, 12, 9), box(5.9, 5.9, 6.9, 10.1, 12.1, 9.1), box(6, 12, 6, 10, 16, 10), box(5.9, 11.9, 5.9, 10.1, 16.1, 10.1), box(10, 6, 7, 12, 12, 9), box(9.9, 5.9, 6.9, 12.1, 12.1, 9.1), box(8, 0, 7, 10, 6, 9),
					box(7.9, -0.1, 6.9, 10.1, 6.1, 9.1), box(6, 0, 7, 8, 6, 9), box(5.9, -0.1, 6.9, 8.1, 6.1, 9.1), box(4, 6, 7, 6, 12, 9), box(3.9, 5.9, 6.9, 6.1, 12.1, 9.1));
			case NORTH -> Shapes.or(box(6, 6, 7, 10, 12, 9), box(5.9, 5.9, 6.9, 10.1, 12.1, 9.1), box(6, 12, 6, 10, 16, 10), box(5.9, 11.9, 5.9, 10.1, 16.1, 10.1), box(4, 6, 7, 6, 12, 9), box(3.9, 5.9, 6.9, 6.1, 12.1, 9.1), box(6, 0, 7, 8, 6, 9),
					box(5.9, -0.1, 6.9, 8.1, 6.1, 9.1), box(8, 0, 7, 10, 6, 9), box(7.9, -0.1, 6.9, 10.1, 6.1, 9.1), box(10, 6, 7, 12, 12, 9), box(9.9, 5.9, 6.9, 12.1, 12.1, 9.1));
			case EAST -> Shapes.or(box(7, 6, 6, 9, 12, 10), box(6.9, 5.9, 5.9, 9.1, 12.1, 10.1), box(6, 12, 6, 10, 16, 10), box(5.9, 11.9, 5.9, 10.1, 16.1, 10.1), box(7, 6, 4, 9, 12, 6), box(6.9, 5.9, 3.9, 9.1, 12.1, 6.1), box(7, 0, 6, 9, 6, 8),
					box(6.9, -0.1, 5.9, 9.1, 6.1, 8.1), box(7, 0, 8, 9, 6, 10), box(6.9, -0.1, 7.9, 9.1, 6.1, 10.1), box(7, 6, 10, 9, 12, 12), box(6.9, 5.9, 9.9, 9.1, 12.1, 12.1));
			case WEST -> Shapes.or(box(7, 6, 6, 9, 12, 10), box(6.9, 5.9, 5.9, 9.1, 12.1, 10.1), box(6, 12, 6, 10, 16, 10), box(5.9, 11.9, 5.9, 10.1, 16.1, 10.1), box(7, 6, 10, 9, 12, 12), box(6.9, 5.9, 9.9, 9.1, 12.1, 12.1), box(7, 0, 8, 9, 6, 10),
					box(6.9, -0.1, 7.9, 9.1, 6.1, 10.1), box(7, 0, 6, 9, 6, 8), box(6.9, -0.1, 5.9, 9.1, 6.1, 8.1), box(7, 6, 4, 9, 12, 6), box(6.9, 5.9, 3.9, 9.1, 12.1, 6.1));
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(world, pos, state, placer, stack);
		if (!world.isClientSide) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PlayerStatueBlockEntity statueEntity) {
				// Check if the item has a custom name (renamed in anvil)
				if (stack.has(net.minecraft.core.component.DataComponents.CUSTOM_NAME) && world.getServer() != null) {
					String customName = stack.getHoverName().getString();
					
					// Check if we have NBT data and if the name matches
					boolean shouldUseNBT = false;
					if (stack.has(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA)) {
						var blockEntityData = stack.get(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA);
						if (blockEntityData != null) {
							var nbt = blockEntityData.copyTag();
							if (nbt.hasUUID("PlayerUUID") && nbt.contains("PlayerName")) {
								String nbtPlayerName = nbt.getString("PlayerName");
								// If NBT name matches the custom name, use the NBT data (it has texture cached)
								if (customName.equals(nbtPlayerName)) {
									shouldUseNBT = true;
									statueEntity.setPlayerData(nbt.getUUID("PlayerUUID"), nbtPlayerName);
									
									// Also load texture if available
									if (nbt.contains("SkinTextureBase64") && nbt.contains("SkinModel")) {
										statueEntity.setPlayerData(
											nbt.getUUID("PlayerUUID"), 
											nbtPlayerName,
											nbt.getString("SkinTextureBase64"),
											nbt.getString("SkinModel")
										);
									}
									return;
								} else {
									// Name differs - clear the stale NBT data from the item
									EramodMod.LOGGER.info("[PlayerStatue] Name mismatch (NBT: {}, Custom: {}), clearing stale data", nbtPlayerName, customName);
									// Set temporary UUID so renderer shows Steve while fetching real data
									statueEntity.setPlayerData(UUID.randomUUID(), customName);
									stack.remove(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA);
									// Force client update
									world.sendBlockUpdated(pos, state, state, 3);
								}
							}
						}
					}
					
					// Name differs or no NBT - fetch fresh data for the renamed player
					if (!shouldUseNBT) {
						// First check cache for this player name
						if (PlayerSkinCache.has(customName)) {
							EramodMod.LOGGER.info("[PlayerStatue] Using cached data for: {}", customName);
							var cachedData = PlayerSkinCache.get(customName);
							statueEntity.setPlayerData(cachedData.getUuid(), cachedData.getName(), 
								cachedData.getTextureBase64(), cachedData.getModel());
							return;
						}
						
						// Try to find player by name
						var targetPlayer = world.getServer().getPlayerList().getPlayerByName(customName);
						if (targetPlayer != null) {
							// Use the named player's data
							statueEntity.setPlayerData(targetPlayer.getUUID(), customName);
						} else {
							// Player not online, set temporary data and fetch UUID from Mojang API
							// This creates a temporary UUID so the renderer shows Steve skin immediately
							statueEntity.setPlayerData(UUID.randomUUID(), customName);
							// Force client update to show Steve skin while fetching
							world.sendBlockUpdated(pos, state, state, 3);
							fetchPlayerUUIDAsync(customName, statueEntity, placer);
						}
					}
				} else {
					// No custom name - check if item has stored NBT data from previous placement
					if (stack.has(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA)) {
						var blockEntityData = stack.get(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA);
						if (blockEntityData != null) {
							var nbt = blockEntityData.copyTag();
							if (nbt.hasUUID("PlayerUUID")) {
								// Use stored player data
								statueEntity.setPlayerData(nbt.getUUID("PlayerUUID"), nbt.getString("PlayerName"));
								return;
							}
						}
					}
					
					// No NBT data, use placer's data
					if (placer instanceof Player player) {
						statueEntity.setPlayerData(player.getUUID(), player.getName().getString());
					}
				}
			}
		}
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlayerStatueBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PlayerStatueBlockEntity be) {
				Containers.dropContents(world, pos, be);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof PlayerStatueBlockEntity be)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(be);
		else
			return 0;
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PlayerStatueBlockEntity statueEntity) {
			String playerName = statueEntity.getPlayerName();
			
			// Check if this player has a custom particle effect
			if (playerName != null && PLAYER_PARTICLES.containsKey(playerName)) {
				var particleType = PLAYER_PARTICLES.get(playerName);
				
				// Different spawn rates based on particle type
				int particleCount = 2; // Default
				if (particleType == ParticleTypes.END_ROD) {
					particleCount = 1; // Reduce end rod particles
				} else if (particleType == ParticleTypes.CHERRY_LEAVES) {
					particleCount = 1; // Reduce cherry leaves
				} else if (particleType == ParticleTypes.HAPPY_VILLAGER) {
					particleCount = 1; // Reduce happy villager
				} else if (particleType == ParticleTypes.ENCHANT) {
					particleCount = 3; // Increase enchant particles
				}
				
				// Spawn custom particles around the statue
				for (int i = 0; i < particleCount; i++) {
					double xOffset = (random.nextDouble() - 0.5) * 0.8;
					double yOffset = random.nextDouble() * 1.5;
					double zOffset = (random.nextDouble() - 0.5) * 0.8;
					
					// Different particle behaviors based on type
					if (particleType == ParticleTypes.FLAME) {
						// Fire particles rise upward
						world.addParticle(particleType,
							pos.getX() + 0.5 + xOffset,
							pos.getY() + yOffset,
							pos.getZ() + 0.5 + zOffset,
							0,
							0.05,
							0
						);
					} else if (particleType == ParticleTypes.HAPPY_VILLAGER) {
						// Happy villager particles pop outward (reduced)
						if (random.nextInt(2) == 0) {
							world.addParticle(particleType,
								pos.getX() + 0.5 + xOffset,
								pos.getY() + yOffset,
								pos.getZ() + 0.5 + zOffset,
								random.nextGaussian() * 0.02,
								random.nextGaussian() * 0.02,
								random.nextGaussian() * 0.02
							);
						}
					} else if (particleType == ParticleTypes.CHERRY_LEAVES) {
						// Cherry leaves fall down (reduced)
						if (random.nextInt(2) == 0) {
							world.addParticle(particleType,
								pos.getX() + 0.5 + xOffset,
								pos.getY() + yOffset + 0.5,
								pos.getZ() + 0.5 + zOffset,
								xOffset * 0.1,
								-0.02,
								zOffset * 0.1
							);
						}
					} else if (particleType == ParticleTypes.END_ROD) {
						// End rod particles drift slowly (reduced)
						if (random.nextInt(2) == 0) {
							world.addParticle(particleType,
								pos.getX() + 0.5 + xOffset,
								pos.getY() + yOffset,
								pos.getZ() + 0.5 + zOffset,
								0,
								0.02,
								0
							);
						}
					} else if (particleType == ParticleTypes.CRIT) {
						// Crit particles swirl around (increased)
						world.addParticle(particleType,
							pos.getX() + 0.5 + xOffset,
							pos.getY() + yOffset,
							pos.getZ() + 0.5 + zOffset,
							xOffset * 0.7,
							0.05,
							zOffset * 0.7
						);

					} else if (particleType == ParticleTypes.SMOKE) 
					{
						// Smoke particles drift upward slowly (increased)
						world.addParticle(particleType,
							pos.getX() + 0.5 + xOffset,
							pos.getY() + yOffset,
							pos.getZ() + 0.5 + zOffset,
							xOffset * 0.3,
							0.03,
							zOffset * 0.3
						);
					} else {
						// Default behavior (enchant-like spiral) - increased
						world.addParticle(particleType,
							pos.getX() + 0.5 + xOffset,
							pos.getY() + yOffset,
							pos.getZ() + 0.5 + zOffset,
							xOffset * 0.5,
							0.05,
							zOffset * 0.5
						);
					}
				}
			}

			// Special case for LOVE person - spawn heart particles (slightly increased)
			if (playerName != null && playerName.equals(LOVE)) {
				// Spawn heart particles around the statue
				if (random.nextInt(2) == 0) { // Changed from 3 to 2
					double xOffset = (random.nextDouble() - 0.5) * 0.6;
					double yOffset = random.nextDouble() * 1.2 + 0.5;
					double zOffset = (random.nextDouble() - 0.5) * 0.6;
					
					world.addParticle(ParticleTypes.HEART,
						pos.getX() + 0.5 + xOffset,
						pos.getY() + yOffset,
						pos.getZ() + 0.5 + zOffset,
						0,
						0.03,
						0
					);
				}
			}
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
		// Get the block entity
		BlockEntity blockEntity = builder.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY);
		
		if (blockEntity instanceof PlayerStatueBlockEntity statueEntity) {
			// Create the item stack using the same method as creative pick
			ItemStack stack = new ItemStack(this);
			statueEntity.saveToItem(stack, builder.getLevel().registryAccess());
			
			// Set custom name as a separate component
			if (statueEntity.getPlayerName() != null && !statueEntity.getPlayerName().isEmpty()) {
				stack.set(DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal(statueEntity.getPlayerName()));
			}
			
			return List.of(stack);
		}
		
		return super.getDrops(state, builder);
	}

	private void fetchPlayerUUIDAsync(String playerName, PlayerStatueBlockEntity statueEntity, LivingEntity placer) {
		CompletableFuture.runAsync(() -> {
			try {
				EramodMod.LOGGER.info("[PlayerStatue] Fetching skin for player: {}", playerName);
				
				HttpClient client = HttpClient.newHttpClient();
				String textureBase64 = null;
				String model = "default";
				UUID playerUUID = null;
				
				// First, try Eradium API for custom skin
				EramodMod.LOGGER.info("[PlayerStatue] Checking Eradium API for custom skin...");
				try {
					HttpRequest eradiumRequest = HttpRequest.newBuilder()
							.uri(URI.create("https://eradium.fr/api/skin-api/skins/" + playerName))
							.GET()
							.timeout(java.time.Duration.ofSeconds(5))
							.build();
					
					HttpResponse<byte[]> eradiumResponse = client.send(eradiumRequest, HttpResponse.BodyHandlers.ofByteArray());
					
					if (eradiumResponse.statusCode() == 200) {
						// Successfully got custom skin from Eradium API
						textureBase64 = java.util.Base64.getEncoder().encodeToString(eradiumResponse.body());
						EramodMod.LOGGER.info("[PlayerStatue] Custom skin found on Eradium API (length: {})", textureBase64.length());
						
						// We still need to get the UUID from Mojang
						HttpRequest uuidRequest = HttpRequest.newBuilder()
								.uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + playerName))
								.GET()
								.build();
						
						HttpResponse<String> uuidResponse = client.send(uuidRequest, HttpResponse.BodyHandlers.ofString());
						if (uuidResponse.statusCode() == 200) {
							JsonObject uuidJson = JsonParser.parseString(uuidResponse.body()).getAsJsonObject();
							String uuidString = uuidJson.get("id").getAsString();
							String formattedUUID = uuidString.replaceFirst(
									"(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
									"$1-$2-$3-$4-$5"
							);
							playerUUID = UUID.fromString(formattedUUID);
							EramodMod.LOGGER.info("[PlayerStatue] Got UUID from Mojang: {}", playerUUID);
						}
					} else {
						EramodMod.LOGGER.info("[PlayerStatue] No custom skin on Eradium API (status: {}), falling back to Mojang", eradiumResponse.statusCode());
					}
				} catch (Exception e) {
					EramodMod.LOGGER.info("[PlayerStatue] Eradium API check failed: {}, falling back to Mojang", e.getMessage());
				}
				
				// If Eradium API didn't work, fall back to Mojang API
				if (textureBase64 == null) {
					EramodMod.LOGGER.info("[PlayerStatue] Fetching from Mojang API...");
					HttpRequest uuidRequest = HttpRequest.newBuilder()
							.uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + playerName))
							.GET()
							.build();

					HttpResponse<String> uuidResponse = client.send(uuidRequest, HttpResponse.BodyHandlers.ofString());
					EramodMod.LOGGER.info("[PlayerStatue] UUID API Response Code: {}", uuidResponse.statusCode());

					if (uuidResponse.statusCode() == 200) {
						JsonObject uuidJson = JsonParser.parseString(uuidResponse.body()).getAsJsonObject();
						String uuidString = uuidJson.get("id").getAsString();
						EramodMod.LOGGER.info("[PlayerStatue] Player UUID (no dashes): {}", uuidString);
						
						// Convert string to UUID (Mojang API returns UUID without dashes)
						String formattedUUID = uuidString.replaceFirst(
								"(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
								"$1-$2-$3-$4-$5"
						);
						playerUUID = UUID.fromString(formattedUUID);
						EramodMod.LOGGER.info("[PlayerStatue] Player UUID (formatted): {}", playerUUID);
						
						String textureUrl = null;
						
						// Fetch skin data from session server
						HttpRequest profileRequest = HttpRequest.newBuilder()
								.uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidString))
								.GET()
								.build();
						
						HttpResponse<String> profileResponse = client.send(profileRequest, HttpResponse.BodyHandlers.ofString());
						EramodMod.LOGGER.info("[PlayerStatue] Session Server Response Code: {}", profileResponse.statusCode());
						
						if (profileResponse.statusCode() == 200) {
							JsonObject profileJson = JsonParser.parseString(profileResponse.body()).getAsJsonObject();
							EramodMod.LOGGER.debug("[PlayerStatue] Profile JSON: {}", profileJson.toString());
							
							if (profileJson.has("properties")) {
								var properties = profileJson.getAsJsonArray("properties");
								EramodMod.LOGGER.info("[PlayerStatue] Properties count: {}", properties.size());
								
								for (int i = 0; i < properties.size(); i++) {
									var property = properties.get(i).getAsJsonObject();
									String propName = property.get("name").getAsString();
									EramodMod.LOGGER.debug("[PlayerStatue] Property {} name: {}", i, propName);
									
									if ("textures".equals(propName)) {
										String textureValue = property.get("value").getAsString();
										EramodMod.LOGGER.info("[PlayerStatue] Found textures property, decoding...");
										
										String decodedTexture = new String(java.util.Base64.getDecoder().decode(textureValue));
										EramodMod.LOGGER.debug("[PlayerStatue] Decoded texture data: {}", decodedTexture);
										
										JsonObject textureJson = JsonParser.parseString(decodedTexture).getAsJsonObject();
										
										if (textureJson.has("textures") && textureJson.getAsJsonObject("textures").has("SKIN")) {
											var skinObj = textureJson.getAsJsonObject("textures").getAsJsonObject("SKIN");
											textureUrl = skinObj.get("url").getAsString();
											EramodMod.LOGGER.info("[PlayerStatue] Skin URL: {}", textureUrl);
											
											// Download the actual skin texture
											HttpRequest textureRequest = HttpRequest.newBuilder()
													.uri(URI.create(textureUrl))
													.GET()
													.build();
											
											HttpResponse<byte[]> textureResponse = client.send(textureRequest, HttpResponse.BodyHandlers.ofByteArray());
											EramodMod.LOGGER.info("[PlayerStatue] Texture download response code: {}", textureResponse.statusCode());
											
											if (textureResponse.statusCode() == 200) {
												textureBase64 = java.util.Base64.getEncoder().encodeToString(textureResponse.body());
												EramodMod.LOGGER.info("[PlayerStatue] Texture downloaded and encoded to base64 (length: {})", textureBase64.length());
											}
											
											// Check for model metadata
											if (skinObj.has("metadata")) {
												var metadata = skinObj.getAsJsonObject("metadata");
												if (metadata.has("model")) {
													model = metadata.get("model").getAsString();
													EramodMod.LOGGER.info("[PlayerStatue] Model type: {}", model);
												}
											}
										}
										break;
									}
								}
							}
						}
					} else {
						EramodMod.LOGGER.info("[PlayerStatue] Player not found on Mojang API");
					}
				}
				
				// Common logic for both Eradium and Mojang paths
				if (playerUUID != null) {
					final String finalTexture = textureBase64;
					final String finalModel = model;
					final UUID finalUUID = playerUUID;
					
					EramodMod.LOGGER.info("[PlayerStatue] Setting player data - UUID: {}, Name: {}, Has Texture: {}, Model: {}", playerUUID, playerName, (finalTexture != null), finalModel);
					
					// Cache the player data for future use
					if (finalTexture != null) {
						PlayerSkinCache.put(playerName, finalUUID, finalTexture, finalModel);
					}
					
					// Set player data on the main thread
					if (statueEntity.getLevel() != null && statueEntity.getLevel().getServer() != null) {
						statueEntity.getLevel().getServer().execute(() -> {
							statueEntity.setPlayerData(finalUUID, playerName, finalTexture, finalModel);
							EramodMod.LOGGER.info("[PlayerStatue] Player data set successfully!");
						});
					}
				} else {
					EramodMod.LOGGER.info("[PlayerStatue] Player not found, falling back to placer");
					// Fallback to placer if player not found
					if (placer instanceof Player player && statueEntity.getLevel() != null && statueEntity.getLevel().getServer() != null) {
						statueEntity.getLevel().getServer().execute(() -> {
							statueEntity.setPlayerData(player.getUUID(), player.getName().getString());
						});
					}
				}
			} catch (Exception e) {
				EramodMod.LOGGER.error("[PlayerStatue] Error fetching player skin: {}", e.getMessage(), e);
				// On error, fallback to placer
				if (placer instanceof Player player) {
					if (statueEntity.getLevel() != null && statueEntity.getLevel().getServer() != null) {
						statueEntity.getLevel().getServer().execute(() -> {
							statueEntity.setPlayerData(player.getUUID(), player.getName().getString());
						});
					}
				}
			}
		});
	}
}