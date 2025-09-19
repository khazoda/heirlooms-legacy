package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* FOR CRAFTING TABLE */
/* Targets player taking a crafted item out while pressing shift */
@Mixin(CraftingMenu.class)
public class MixinCraftingMenu {
  @Inject(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V", shift = At.Shift.AFTER))
  private void heirlooms$onShiftClickCrafted(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
    if (index == 0) {
      Slot slot = ((CraftingMenu) (Object) this).getSlot(0);
      if (slot.hasItem()) {
        SlotResultModifier.handleCraftedItem(player, slot.getItem());
      }
    }
  }
}