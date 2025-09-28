package com.khazoda.heirlooms;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class HeirloomsModFabric implements ModInitializer {

  @Override
  public void onInitialize() {
    Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "crafted_timestamp"),
            HeirloomsMod.CRAFTED_TIMESTAMP
    );
    Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "crafted_by"),
            HeirloomsMod.CRAFTED_BY
    );
    Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "enchanted_timestamp"),
            HeirloomsMod.ENCHANTED_TIMESTAMP
    );
    Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "enchanted_by"),
            HeirloomsMod.ENCHANTED_BY
    );
  }
}
