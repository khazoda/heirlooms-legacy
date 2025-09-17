package com.khazoda.heirlooms;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.world.item.TooltipFlag;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TooltipHandler implements ItemTooltipCallback {

  public static void register() {
    ItemTooltipCallback.EVENT.register(new TooltipHandler());
  }

  @Override
  public void getTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> list) {
    String timestamp = itemStack.get(HeirloomsMod.CRAFTED_TIMESTAMP);

    if (timestamp != null) {
      try {
        Instant instant = Instant.parse(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        String formattedTime = formatter.format(instant);
        list.add(Component.literal("Crafted: " + formattedTime));
      } catch (Exception e) {
        list.add(Component.literal("Crafted: " + timestamp));
      }
    }
  }
}