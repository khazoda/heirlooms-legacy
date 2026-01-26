package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.HeirloomsState;
import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class MixinInventory {

  @Final
  @Shadow
  public Player player;

  @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"))
  private void heirlooms$onAddItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
    heirlooms$capture(stack);
  }

  @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"))
  private void heirlooms$onAddItemAtIndex(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
    heirlooms$capture(stack);
  }

  @Inject(method = "setItem", at = @At("HEAD"))
  private void heirlooms$onSetItem(int slot, ItemStack stack, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
    heirlooms$capture(stack);
  }

  @Unique
  private void heirlooms$capture(ItemStack stack) {
    if (stack != null && !stack.isEmpty()) {
      Player capturingPlayer = HeirloomsState.getCapturingPlayer();
      if (capturingPlayer != null && capturingPlayer == this.player) {
        SlotResultModifier.handleCraftedItem(capturingPlayer, stack);
      }
    }
  }
}
