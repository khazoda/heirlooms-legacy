package com.khazoda.heirlooms.block.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DisplayRackRenderState extends BlockEntityRenderState {
  @Nullable
  public ItemStackRenderState item;
  public Direction facing = Direction.NORTH;
  public BlockState renderedBlockState;
  public boolean isBlockItem;
}