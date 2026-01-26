package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.HeirloomsCommon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneMenu.class)
public class MixinGrindstoneMenu {
  @Inject(method = "removeNonCurses", at = @At(value = "RETURN"))
  private void heirlooms$onEnchantmentRemoved(ItemStack item, int damage, int count, CallbackInfoReturnable<ItemStack> cir) {
    ItemStack result = cir.getReturnValue();
    if (result.isEmpty()) return;

    CompoundTag tag = result.getTag();
    if (tag != null && (tag.contains(HeirloomsCommon.ENCHANTED_TIMESTAMP) || tag.contains(HeirloomsCommon.ENCHANTED_BY))) {
      tag.remove(HeirloomsCommon.ENCHANTED_TIMESTAMP);
      tag.remove(HeirloomsCommon.ENCHANTED_BY);
    }
  }
}