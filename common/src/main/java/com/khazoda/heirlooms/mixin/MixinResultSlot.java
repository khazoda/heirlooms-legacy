package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.Constants;
import com.khazoda.heirlooms.HeirloomsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

@Mixin(ResultSlot.class)
public class MixinResultSlot {
  @Inject(
          method = "onTake",
          at = @At("HEAD")
  )
  private void heirlooms$onItemCrafted(Player player, ItemStack stack, CallbackInfo ci) {
    if (!player.level().isClientSide && !stack.isEmpty()) {
      stack.set(HeirloomsMod.CRAFTED_TIMESTAMP, Instant.now().toString());
      System.out.println("Item Crafted: " + stack + " at " + Instant.now());
    }
  }
}