package com.khazoda.heirlooms.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class DisplayCaseBlock extends BaseEntityBlock {
  public static final MapCodec<DisplayCaseBlock> CODEC = simpleCodec(DisplayCaseBlock::new);
  public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

  public DisplayCaseBlock(BlockBehaviour.Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new DisplayCaseBlockEntity(pos, state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity instanceof DisplayCaseBlockEntity displayCase) {
      if (level.isClientSide()) return InteractionResult.SUCCESS;
      ItemStack currentItem = displayCase.getItem();
      if (currentItem.isEmpty() && !stack.isEmpty()) {
        ItemStack itemToInsert = stack.split(1);
        displayCase.setItem(itemToInsert);
        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
        displayCase.setChanged();
        return InteractionResult.SUCCESS;
      } else if (!currentItem.isEmpty()) {
        ItemStack handStack = player.getItemInHand(hand);

        if (handStack.isEmpty()) {
          player.setItemInHand(hand, currentItem);
          displayCase.setItem(ItemStack.EMPTY);
          level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
        } else {
          ItemStack toPlace = handStack.split(1);
          displayCase.setItem(toPlace);
          if (handStack.isEmpty()) {
            player.setItemInHand(hand, currentItem);
          } else {
            if (!player.getInventory().add(currentItem)) {
              Block.popResource(level, pos, currentItem);
            }
          }
          level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        displayCase.setChanged();
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
    dropBlockContents(level, pos);
    super.onExplosionHit(state, level, pos, explosion, dropConsumer);
  }

  @Override
  public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    dropBlockContents(level, pos);
    super.playerWillDestroy(level, pos, state, player);
    return state;
  }

  private void dropBlockContents(Level level, BlockPos pos) {
    if (!level.isClientSide()) {
      BlockEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntity instanceof DisplayCaseBlockEntity displayCase) {
        if (!displayCase.getItem().isEmpty()) {
          Block.popResource(level, pos, displayCase.getItem());
          displayCase.setItem(ItemStack.EMPTY);
        }
      }
    }
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState neighborState, Direction side) {
    return neighborState.is(this) ? true : super.skipRendering(state, neighborState, side);
  }

  @Override
  public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
    return 1.0F;
  }
}