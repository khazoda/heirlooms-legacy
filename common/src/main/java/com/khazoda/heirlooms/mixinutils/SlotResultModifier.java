package com.khazoda.heirlooms.mixinutils;

import com.khazoda.heirlooms.HeirloomsCommon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.time.Instant;

public class SlotResultModifier {
  public static void handleCraftedItem(Player player, ItemStack stack) {
    if (player.level().isClientSide()) return;
    if (stack.isEmpty()) return;
    if (stack.getMaxStackSize() > 1) return;

    CompoundTag tag = stack.getOrCreateTag();
    if (tag.contains(HeirloomsCommon.CRAFTED_TIMESTAMP) || tag.contains(HeirloomsCommon.CRAFTED_BY)) return;

    tag.putString(HeirloomsCommon.CRAFTED_TIMESTAMP, Instant.now().toString());
    tag.putString(HeirloomsCommon.CRAFTED_BY, player.getGameProfile().getName());
  }

  public static void handleEnchantedItem(Player player, ItemStack stack) {
    if (player.level().isClientSide()) return;
    if (stack.isEmpty()) return;
    if (stack.getMaxStackSize() > 1) return;

    CompoundTag tag = stack.getOrCreateTag();
    if (tag.contains(HeirloomsCommon.ENCHANTED_TIMESTAMP) || tag.contains(HeirloomsCommon.ENCHANTED_BY)) return;

    tag.putString(HeirloomsCommon.ENCHANTED_TIMESTAMP, Instant.now().toString());
    tag.putString(HeirloomsCommon.ENCHANTED_BY, player.getGameProfile().getName());
  }
}