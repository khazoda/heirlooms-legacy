package com.khazoda.heirlooms;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
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
    List<Component> list = event.getToolTip();

    String crafted_timestamp = stack.get(HeirloomsMod.CRAFTED_TIMESTAMP);
    String crafted_by = stack.get(HeirloomsMod.CRAFTED_BY);
    int craftedTextColour = 9068607;

    String enchanted_timestamp = stack.get(HeirloomsMod.ENCHANTED_TIMESTAMP);
    String enchanted_by = stack.get(HeirloomsMod.ENCHANTED_BY);
    int enchantedTextColour = 4157834;

    /* Only apply to items with at least one of my data components */
    if (crafted_timestamp == null && enchanted_timestamp == null) return;

    /* Forge correct button prompts based on player's current pressed key(s) */
    MutableComponent buttonPrompt = Component.empty();
    boolean pressedCTRL = InputConstants.isKeyDown(Minecraft.getInstance().getWindow(),InputConstants.KEY_LCONTROL);
    boolean pressedALT = InputConstants.isKeyDown(Minecraft.getInstance().getWindow(),InputConstants.KEY_LALT);
    boolean existsCraftData = crafted_timestamp != null;
    boolean existsEnchantData = enchanted_timestamp != null;
    if (existsCraftData && !pressedCTRL) buttonPrompt.append(Component.literal("[ᴄᴛʀʟ]").withColor(craftedTextColour));
    if (existsCraftData && !pressedCTRL && existsEnchantData && !pressedALT) buttonPrompt.append(Component.literal(" "));
    if (existsEnchantData && !pressedALT) buttonPrompt.append(Component.literal("[ᴀʟᴛ]").withColor(enchantedTextColour));
    if (!buttonPrompt.getString().isEmpty()) list.add(buttonPrompt);


    /* Add crafting tooltip */
    if (crafted_timestamp != null) {
      if (crafted_by != null) {
        try {
          if (pressedCTRL) {
            Instant instant = Instant.parse(crafted_timestamp);
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

            int year = zonedDateTime.getYear();
            int month = zonedDateTime.getMonthValue();
            int day = zonedDateTime.getDayOfMonth();

            String languageCode = Minecraft.getInstance().getLanguageManager().getSelected();
            Locale locale = Locale.forLanguageTag(languageCode.replace("_", "-"));
            String monthName = zonedDateTime.getMonth().getDisplayName(TextStyle.FULL, locale);

            list.add(Component.translatable("tooltip.heirlooms.crafted_by", crafted_by).withColor(craftedTextColour));
            list.add(Component.translatable("tooltip.heirlooms.date", day, month, monthName, year).withColor(craftedTextColour));
          }
        } catch (Exception ignored) {
          System.out.println("Heirlooms mod error [1] - please report on the issue tracker");
        }
      }
    }

    /* Add enchantment tooltip */
    if (enchanted_timestamp != null) {
      if (enchanted_by != null) {
        try {
          if (pressedALT) {
            Instant instant = Instant.parse(enchanted_timestamp);
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

            int year = zonedDateTime.getYear();
            int month = zonedDateTime.getMonthValue();
            int day = zonedDateTime.getDayOfMonth();

            String languageCode = Minecraft.getInstance().getLanguageManager().getSelected();
            Locale locale = Locale.forLanguageTag(languageCode.replace("_", "-"));
            String monthName = zonedDateTime.getMonth().getDisplayName(TextStyle.FULL, locale);

            list.add(Component.translatable("tooltip.heirlooms.enchanted_by", enchanted_by).withColor(enchantedTextColour));
            list.add(Component.translatable("tooltip.heirlooms.date", day, month, monthName, year).withColor(enchantedTextColour));
          }
        } catch (Exception ignored) {
          System.out.println("Heirlooms mod error [1] - please report on the issue tracker");
        }
      }
    }
  }
}