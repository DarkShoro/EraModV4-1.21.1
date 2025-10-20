package fr.eradium.eramod.block;

import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import fr.eradium.eramod.init.EramodModBlocks;
import net.minecraft.core.BlockPos;

public class GeanaLogBlock extends Block {
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

	public GeanaLogBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE).sound(SoundType.WOOD).strength(2f).instrument(NoteBlockInstrument.BASS));
		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AXIS);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(AXIS, context.getClickedFace().getAxis());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return RotatedPillarBlock.rotatePillar(state, rot);
	}

	// On use with axe: Stripped geana wood
	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, 
			InteractionHand hand, BlockHitResult hit) {
		
		// Check if the item is an axe
		if (stack.is(ItemTags.AXES)) {
			// Get the stripped variant block (assuming it exists)
			// You'll need to create STRIPPED_GEANA_WOOD in your blocks registry
			// For now, let's use a placeholder or regular wood
			Block strippedBlock = EramodModBlocks.GEANA_STRIPPED_LOG.get();
			
			if (strippedBlock != null) {
				// Play stripping sound
				world.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
				
				// Replace the block with stripped variant, preserving the axis
				BlockState strippedState = strippedBlock.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
				world.setBlock(pos, strippedState, 11);
				
				// Damage the axe
				if (player != null && !player.getAbilities().instabuild) {
					stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
				}
				
				return ItemInteractionResult.sidedSuccess(world.isClientSide);
			}
		}
		
		return super.useItemOn(stack, state, world, pos, player, hand, hit);
	}
}