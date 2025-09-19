package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* FOR SMITHING TABLE */
/* Targets player taking a crafted item out while holding shift */
@Mixin(ItemCombinerMenu.class)
public class MixinItemCombinerMenu {

  @Inject(
          method = "quickMoveStack",
          at = @At(
                  value = "INVOKE",
                  target = "Lnet/minecraft/world/inventory/ItemCombinerMenu;getResultSlot()I",
                  shift = At.Shift.AFTER
          )
  )
  private void heirlooms$onShiftClickSmithed(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
    ItemCombinerMenu menu = (ItemCombinerMenu)(Object)this;
    if (index == menu.getResultSlot()) {
      Slot slot = menu.getSlot(index);
      if (slot.hasItem()) {
        SlotResultModifier.handleCraftedItem(player, slot.getItem());
      }
    }
  }
}