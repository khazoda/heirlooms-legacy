package com.khazoda.heirlooms;

import com.khazoda.heirlooms.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class HeirloomsCommon {
    public static void init() {


        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Constants.LOG.info("- Heirlooms Loaded -");
        }
    }
}