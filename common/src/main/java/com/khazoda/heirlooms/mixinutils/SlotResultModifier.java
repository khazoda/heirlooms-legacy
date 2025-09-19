package com.khazoda.heirlooms.mixinutils;

import com.khazoda.heirlooms.HeirloomsMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.time.Instant;

public class SlotResultModifier {
  public static void handleCraftedItem(Player player, ItemStack stack) {
    if (player.level().isClientSide()) return;
    if (stack.isEmpty()) return;
    if (stack.getMaxStackSize() > 1) return;
    if (stack.has(HeirloomsMod.CRAFTED_TIMESTAMP) || stack.has(HeirloomsMod.CRAFTED_BY)) return;

    stack.set(HeirloomsMod.CRAFTED_TIMESTAMP, Instant.now().toString());
    stack.set(HeirloomsMod.CRAFTED_BY, player.getGameProfile().getName());
    player.sendSystemMessage(Component.literal("CRAFTED:" + stack.getItem()));

  }
}
