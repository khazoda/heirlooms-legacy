package com.khazoda.heirlooms.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;

public class DataComponentRegistry {

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
  public static final DataComponentType<String> ENCHANTED_TIMESTAMP =
      DataComponentType.<String>builder()
          .persistent(Codec.STRING)
          .networkSynchronized(ByteBufCodecs.STRING_UTF8)
          .build();
  public static final DataComponentType<String> ENCHANTED_BY =
      DataComponentType.<String>builder()
          .persistent(Codec.STRING)
          .networkSynchronized(ByteBufCodecs.STRING_UTF8)
          .build();
}
