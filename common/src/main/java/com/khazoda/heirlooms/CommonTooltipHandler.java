package com.khazoda.heirlooms;

import com.khazoda.heirlooms.registry.DataComponentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class CommonTooltipHandler {

  public static final int CRAFTED_TEXT_COLOR = 9068607;
  public static final int ENCHANTED_TEXT_COLOR = 4157834;

  private static String cachedLanguageCode = null;
  private static Locale cachedLocale = null;

  public static void handleTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag flag, boolean isCtrlPressed, boolean isAltPressed) {
    String craftedTimestamp = stack.get(DataComponentRegistry.CRAFTED_TIMESTAMP);
    String craftedBy = stack.get(DataComponentRegistry.CRAFTED_BY);
    String enchantedTimestamp = stack.get(DataComponentRegistry.ENCHANTED_TIMESTAMP);
    String enchantedBy = stack.get(DataComponentRegistry.ENCHANTED_BY);

    // Only apply to items with at least one Heirlooms data component
    if (craftedTimestamp == null && enchantedTimestamp == null) return;

    // Add button prompts
    addButtonPrompts(tooltip, craftedTimestamp != null, enchantedTimestamp != null, isCtrlPressed, isAltPressed);

    // Add crafting tooltip if CTRL is pressed
    if (craftedTimestamp != null && craftedBy != null && isCtrlPressed)
      addTimestampTooltip(tooltip, craftedTimestamp, craftedBy, "tooltip.heirlooms.crafted_by", CRAFTED_TEXT_COLOR);

    // Add enchantment tooltip if ALT is pressed
    if (enchantedTimestamp != null && enchantedBy != null && isAltPressed)
      addTimestampTooltip(tooltip, enchantedTimestamp, enchantedBy, "tooltip.heirlooms.enchanted_by", ENCHANTED_TEXT_COLOR);

  }

  private static void addButtonPrompts(List<Component> tooltip, boolean hasCraftData, boolean hasEnchantData, boolean isCtrlPressed, boolean isAltPressed) {
    MutableComponent buttonPrompt = Component.empty();

    if (hasCraftData && !isCtrlPressed) buttonPrompt.append(Component.literal("[ᴄᴛʀʟ]").withColor(CRAFTED_TEXT_COLOR));
    if (hasCraftData && !isCtrlPressed && hasEnchantData && !isAltPressed) buttonPrompt.append(Component.literal(" "));
    if (hasEnchantData && !isAltPressed)
      buttonPrompt.append(Component.literal("[ᴀʟᴛ]").withColor(ENCHANTED_TEXT_COLOR));
    if (!buttonPrompt.getString().isEmpty()) tooltip.add(buttonPrompt);
  }

  public static void updateCachedLocale() {
    try {
      String languageCode = net.minecraft.client.Minecraft.getInstance().getLanguageManager().getSelected();
      if (!languageCode.equals(cachedLanguageCode)) {
        cachedLanguageCode = languageCode;
        cachedLocale = Locale.forLanguageTag(languageCode.replace("_", "-"));
      }
    } catch (Exception e) {
      cachedLanguageCode = "en_us";
      cachedLocale = Locale.ENGLISH;
    }
  }

  private static Locale getCachedLocale() {
    if (cachedLocale == null) {
      updateCachedLocale();
    }
    return cachedLocale;
  }

  private static void addTimestampTooltip(List<Component> tooltip, String timestamp, String creator, String translationKey, int color) {
    try {
      Instant instant = Instant.parse(timestamp);
      ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

      int year = zonedDateTime.getYear();
      int month = zonedDateTime.getMonthValue();
      int day = zonedDateTime.getDayOfMonth();

      /* Get localized month name */
      Locale locale = getCachedLocale();
      String monthName = zonedDateTime.getMonth().getDisplayName(TextStyle.FULL, locale);

      tooltip.add(Component.translatable(translationKey, creator).withColor(color));
      tooltip.add(Component.translatable("tooltip.heirlooms.date", getFormattedDay(day, locale), month, monthName, year).withColor(color));

    } catch (Exception e) {
      System.out.println("Heirlooms mod error [1] - please report on the issue tracker");
    }
  }

  // Handle en_us and en_gb ordinal suffixes
  private static String getFormattedDay(int day, Locale locale) {
    if (locale.getLanguage().equals("en")) {
      String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
      return switch (day % 100) {
        case 11, 12, 13 -> day + "th";
        default -> day + suffixes[day % 10];
      };
    } else {
      return String.valueOf(day);
    }
  }
}