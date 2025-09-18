package com.khazoda.heirlooms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class TooltipHandler {

  @SubscribeEvent
  public static void onItemTooltip(ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();
    String timestamp = stack.get(HeirloomsMod.CRAFTED_TIMESTAMP);
    String crafted_by = stack.get(HeirloomsMod.CRAFTED_BY);

    if (timestamp == null) return;
    if (crafted_by == null) return;
    try {
      if (!Screen.hasControlDown()) {
        event.getToolTip().add(Component.literal("[ᴄᴛʀʟ]").withColor(6776679));
      } else {
        Instant instant = Instant.parse(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonthValue();
        int day = zonedDateTime.getDayOfMonth();

        String languageCode = Minecraft.getInstance().getLanguageManager().getSelected();
        Locale locale = Locale.forLanguageTag(languageCode.replace("_", "-"));
        String monthName = zonedDateTime.getMonth().getDisplayName(TextStyle.FULL, locale);

        event.getToolTip().add(Component.translatable("tooltip.heirlooms.crafted_by", crafted_by).withColor(6776679));
        event.getToolTip().add(Component.translatable("tooltip.heirlooms.crafted_date", day, month, monthName, year).withColor(6776679));
      }
    } catch (Exception ignored) {
    }
  }
}