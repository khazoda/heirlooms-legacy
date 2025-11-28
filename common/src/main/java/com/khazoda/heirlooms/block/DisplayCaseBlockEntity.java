package com.khazoda.heirlooms.block;

import com.khazoda.heirlooms.Constants;
import com.khazoda.heirlooms.registry.MainRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import static com.khazoda.heirlooms.Constants.LOG;

public class DisplayCaseBlockEntity extends BlockEntity implements ItemOwner {
  private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

  public DisplayCaseBlockEntity(BlockPos pos, BlockState state) {
    super(MainRegistry.DISPLAY_CASE_BE.get(), pos, state);
  }

  public ItemStack getItem() {
    return items.getFirst();
  }

  public void setItem(ItemStack stack) {
    items.set(0, stack);
    this.setChanged();
    if (this.level != null) {
      this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }
  }

  /* Persistence & Sync */
  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.items.clear();
    ContainerHelper.loadAllItems(input, this.items);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    ContainerHelper.saveAllItems(output, this.items, true);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    try (ProblemReporter.ScopedCollector collector = new ProblemReporter.ScopedCollector(this.problemPath(), LOG)) {
      TagValueOutput output = TagValueOutput.createWithContext(collector, registries);
      ContainerHelper.saveAllItems(output, this.items, true);
      return output.buildResult();
    }
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  /* Component Handling */
  @Override
  protected void applyImplicitComponents(DataComponentGetter componentGetter) {
    super.applyImplicitComponents(componentGetter);
    componentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.items);
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder components) {
    super.collectImplicitComponents(components);
    components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
  }

  @Override
  public void removeComponentsFromTag(ValueOutput output) {
    output.discard("Items");
  }

  /* ItemOwner Implementation */
  @Override
  public Level level() {
    return this.getLevel();
  }

  @Override
  public Vec3 position() {
    return this.getBlockPos().getCenter();
  }

  @Override
  public float getVisualRotationYInDegrees() {
    Direction facing = this.getBlockState().getValue(DisplayCaseBlock.FACING);
    return facing.getOpposite().toYRot();
  }
}