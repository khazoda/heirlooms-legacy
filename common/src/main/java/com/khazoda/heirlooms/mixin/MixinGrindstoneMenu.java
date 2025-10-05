package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.HeirloomsMod;
import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* FOR GRINDSTONE */
/* Targets the method that's called to disenchant an item */
@Mixin(GrindstoneMenu.class)
public class MixinGrindstoneMenu {
  @Inject(method = "removeNonCursesFrom", at = @At(value = "RETURN"))
  private void heirlooms$onEnchantmentRemoved(ItemStack item, CallbackInfoReturnable<ItemStack> cir) {
    if (item.has(HeirloomsMod.ENCHANTED_TIMESTAMP) || item.has(HeirloomsMod.ENCHANTED_BY)) {
      item.remove(HeirloomsMod.ENCHANTED_TIMESTAMP);
      item.remove(HeirloomsMod.ENCHANTED_BY);
    }
  }
}