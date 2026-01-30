package com.khazoda.heirlooms;

import com.khazoda.heirlooms.platform.Services;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;


public class HeirloomsMod {



  public static void init() {
    if (Services.PLATFORM.isModLoaded("heirlooms")) Constants.LOG.info("- Heirlooms Loaded -");
  }
}