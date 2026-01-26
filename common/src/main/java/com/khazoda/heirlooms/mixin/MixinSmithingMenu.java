package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* FOR SMITHING TABLE */
/* Targets player taking a crafted item out without holding shift */
@Mixin(SmithingMenu.class)
public class MixinSmithingMenu {
  @Inject(method = "onTake", at = @At("HEAD"))
  private void heirlooms$onItemSmithed(Player player, ItemStack stack, CallbackInfo ci) {
    SlotResultModifier.handleCraftedItem(player, stack);
  }
}