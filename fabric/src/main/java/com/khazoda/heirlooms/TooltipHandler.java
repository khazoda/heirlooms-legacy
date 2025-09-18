package com.khazoda.heirlooms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.world.item.TooltipFlag;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class TooltipHandler implements ItemTooltipCallback {

  public static void register() {
    ItemTooltipCallback.EVENT.register(new TooltipHandler());
  }

  @Override
  public void getTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> list) {
    String timestamp = itemStack.get(HeirloomsMod.CRAFTED_TIMESTAMP);
    String crafted_by = itemStack.get(HeirloomsMod.CRAFTED_BY);

    if (timestamp == null) return;
    if (crafted_by == null) return;
    try {
      if (!Screen.hasControlDown()) {
        list.add(Component.literal("[ᴄᴛʀʟ]").withColor(6776679));
      } else {
        Instant instant = Instant.parse(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonthValue();
        int day = zonedDateTime.getDayOfMonth();

        String languageCode = Minecraft.getInstance().getLanguageManager().getSelected();
        Locale locale = Locale.forLanguageTag(languageCode.replace("_", "-"));
        String monthName = zonedDateTime.getMonth().getDisplayName(TextStyle.FULL, locale);

        list.add(Component.translatable("tooltip.heirlooms.crafted_by", crafted_by).withColor(6776679));
        list.add(Component.translatable("tooltip.heirlooms.crafted_date", day, month, monthName, year).withColor(6776679));
      }
    } catch (Exception ignored) {
    }
  }
}