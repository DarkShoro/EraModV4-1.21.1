package fr.eradium.eramod.block.entity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import java.util.stream.IntStream;
import java.util.UUID;

import fr.eradium.eramod.init.EramodModBlockEntities;

public class PlayerStatueBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
	private NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);
	private UUID playerUUID;
	private String playerName;
	private String skinTextureBase64;
	private String skinModel = "default"; // "default" or "slim"

	public PlayerStatueBlockEntity(BlockPos position, BlockState state) {
		super(EramodModBlockEntities.PLAYER_STATUE.get(), position, state);
	}

	public void setPlayerData(UUID uuid, String name) {
		this.playerUUID = uuid;
		this.playerName = name;
		setChanged();
	}

	public void setPlayerData(UUID uuid, String name, String textureBase64, String model) {
		this.playerUUID = uuid;
		this.playerName = name;
		this.skinTextureBase64 = textureBase64;
		this.skinModel = model;
		setChanged();
		// Force client update
		if (this.level != null && !this.level.isClientSide) {
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
		}
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getSkinTextureBase64() {
		return skinTextureBase64;
	}

	public String getSkinModel() {
		return skinModel;
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider lookupProvider) {
		super.loadAdditional(compound, lookupProvider);
		if (!this.tryLoadLootTable(compound))
			this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compound, this.stacks, lookupProvider);
		
		// Load player data
		if (compound.hasUUID("PlayerUUID")) {
			this.playerUUID = compound.getUUID("PlayerUUID");
		}
		if (compound.contains("PlayerName")) {
			this.playerName = compound.getString("PlayerName");
		}
		if (compound.contains("SkinTextureBase64")) {
			this.skinTextureBase64 = compound.getString("SkinTextureBase64");
		}
		if (compound.contains("SkinModel")) {
			this.skinModel = compound.getString("SkinModel");
		}
	}

	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider lookupProvider) {
		super.saveAdditional(compound, lookupProvider);
		if (!this.trySaveLootTable(compound)) {
			ContainerHelper.saveAllItems(compound, this.stacks, lookupProvider);
		}
		
		// Save player data
		if (this.playerUUID != null) {
			compound.putUUID("PlayerUUID", this.playerUUID);
		}
		if (this.playerName != null) {
			compound.putString("PlayerName", this.playerName);
		}
		if (this.skinTextureBase64 != null) {
			compound.putString("SkinTextureBase64", this.skinTextureBase64);
		}
		if (this.skinModel != null) {
			compound.putString("SkinModel", this.skinModel);
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return this.saveWithFullMetadata(lookupProvider);
	}

	@Override
	public int getContainerSize() {
		return stacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.stacks)
			if (!itemstack.isEmpty())
				return false;
		return true;
	}

	@Override
	public Component getDefaultName() {
		return Component.literal("player_statue");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return ChestMenu.threeRows(id, inventory);
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Player Statue");
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.stacks;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> stacks) {
		this.stacks = stacks;
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return IntStream.range(0, this.getContainerSize()).toArray();
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemstack, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemstack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack itemstack, Direction direction) {
		return true;
	}
}