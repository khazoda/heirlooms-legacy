package com.khazoda.heirlooms;

import com.khazoda.heirlooms.platform.Services;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;


public class HeirloomsMod {

  public static final DataComponentType<String> CRAFTED_TIMESTAMP =
          DataComponentType.<String>builder()
                  .persistent(Codec.STRING)
                  .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                  .build();
  public static final DataComponentType<String> CRAFTED_BY =
          DataComponentType.<String>builder()
                  .persistent(Codec.STRING)
                  .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                  .build();

  public static void init() {
    if (Services.PLATFORM.isModLoaded("heirlooms")) Constants.LOG.info("- Heirlooms Loaded -");
  }
}