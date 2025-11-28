package com.khazoda.heirlooms.registry;

import com.khazoda.heirlooms.HeirloomsCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class TabRegistry {
  public static final Supplier<CreativeModeTab> HEIRLOOMS_TAB = HeirloomsCommon.REGISTRARS
          .get(Registries.CREATIVE_MODE_TAB)
          .register("main", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                  .title(Component.translatable("heirlooms.itemGroup"))
                  .icon(() -> MainRegistry.DISPLAY_CASE_ITEM.get().getDefaultInstance())
                  .displayItems((parameters, output) -> {
                    output.accept(MainRegistry.DISPLAY_CASE_ITEM.get());
                  })
                  .build());
  public static void init() {
  }
}
