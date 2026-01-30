package com.khazoda.heirlooms.mixinutils;

import com.khazoda.heirlooms.HeirloomsMod;
import com.khazoda.heirlooms.registry.DataComponentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.time.Instant;

public class SlotResultModifier {
  public static void handleCraftedItem(Player player, ItemStack stack) {
    if (player.level().isClientSide()) return;
    if (stack.isEmpty()) return;
    if (stack.getMaxStackSize() > 1) return;
    if (stack.has(DataComponentRegistry.CRAFTED_TIMESTAMP) || stack.has(DataComponentRegistry.CRAFTED_BY)) return;

    stack.set(DataComponentRegistry.CRAFTED_TIMESTAMP, Instant.now().toString());
    stack.set(DataComponentRegistry.CRAFTED_BY, player.getGameProfile().getName());
  }

  public static void handleEnchantedItem(Player player, ItemStack stack) {
    if (player.level().isClientSide()) return;
    if (stack.isEmpty()) return;
    if (stack.getMaxStackSize() > 1) return;
    if (stack.has(DataComponentRegistry.ENCHANTED_TIMESTAMP) || stack.has(DataComponentRegistry.ENCHANTED_BY)) return;

    stack.set(DataComponentRegistry.ENCHANTED_TIMESTAMP, Instant.now().toString());
    stack.set(DataComponentRegistry.ENCHANTED_BY, player.getGameProfile().getName());
  }
}
