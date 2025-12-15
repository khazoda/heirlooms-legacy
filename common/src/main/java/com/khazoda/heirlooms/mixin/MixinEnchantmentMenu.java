package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* FOR Enchanting TABLE */
/* Targets player pressing the button to enchant their item  */
@Mixin(EnchantmentMenu.class)
public class MixinEnchantmentMenu {
  @Inject(method = "clickMenuButton", at = @At("RETURN"))
  private void heirlooms$onEnchanted(Player player, int id, CallbackInfoReturnable<Boolean> cir) {
    Slot slot = ((EnchantmentMenu) (Object) this).getSlot(0);
    if (slot.hasItem()) {
      SlotResultModifier.handleEnchantedItem(player, slot.getItem());
    }
  }
}