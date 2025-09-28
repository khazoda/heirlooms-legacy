package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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