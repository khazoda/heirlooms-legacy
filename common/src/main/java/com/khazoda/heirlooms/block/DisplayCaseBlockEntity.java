package com.khazoda.heirlooms.block;

import com.khazoda.heirlooms.registry.MainRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class DisplayCaseBlockEntity extends BlockEntity implements Container {
  private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

  public DisplayCaseBlockEntity(BlockPos pos, BlockState state) {
    super(MainRegistry.DISPLAY_CASE_BE.get(), pos, state);
  }

  private void inventoryChanged() {
    this.setChanged();
    if (this.level != null) {
      // Send a packet to the client to refresh the DisplayCaseRenderer (for things like hoppers)
      this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }
  }

  /* Container Implementation */
  @Override
  public int getContainerSize() {
    return 1;
  }

  @Override
  public boolean isEmpty() {
    return this.items.getFirst().isEmpty();
  }

  @Override
  public ItemStack getItem(int slot) {
    return this.items.get(slot);
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    ItemStack stack = ContainerHelper.removeItem(this.items, slot, amount);
    if (!stack.isEmpty()) {
      this.inventoryChanged();
    }
    return stack;
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    ItemStack stack = ContainerHelper.takeItem(this.items, slot);
    if (!stack.isEmpty()) {
      this.inventoryChanged();
    }
    return stack;
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    this.items.set(slot, stack);
    stack.limitSize(this.getMaxStackSize(stack));
    this.inventoryChanged();

    if (this.level != null) {
      if (!this.level.isClientSide()) {
        this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
      }
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return Container.stillValidBlockEntity(this, player);
  }

  @Override
  public void clearContent() {
    this.items.clear();
    this.inventoryChanged();
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
    return this.saveWithoutMetadata(registries);
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
}