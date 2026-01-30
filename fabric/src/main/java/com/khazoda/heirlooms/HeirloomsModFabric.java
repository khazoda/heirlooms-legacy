package com.khazoda.heirlooms;

import com.khazoda.heirlooms.registry.DataComponentRegistry;
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
        DataComponentRegistry.CRAFTED_TIMESTAMP
    );
    Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "crafted_by"),
        DataComponentRegistry.CRAFTED_BY
    );
    Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "enchanted_timestamp"),
        DataComponentRegistry.ENCHANTED_TIMESTAMP
    );
    Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "enchanted_by"),
        DataComponentRegistry.ENCHANTED_BY
    );
  }
}
