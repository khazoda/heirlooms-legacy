package com.khazoda.heirlooms;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class TooltipHandler {

  @SubscribeEvent
  public static void onItemTooltip(ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();
    String timestamp = stack.get(HeirloomsMod.CRAFTED_TIMESTAMP);

    if (timestamp != null) {
      try {
        // Parse and format the timestamp
        Instant instant = Instant.parse(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        String formattedTime = formatter.format(instant);
        event.getToolTip().add(Component.literal("Crafted: " + formattedTime));
      } catch (Exception e) {
        event.getToolTip().add(Component.literal("Crafted: " + timestamp));
      }
    }
  }
}