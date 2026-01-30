package com.khazoda.heirlooms;


import com.khazoda.heirlooms.registry.DataComponentRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class HeirloomsModNeoForge {

  public HeirloomsModNeoForge(IEventBus eventBus) {
    HeirloomsMod.init();

    // Register Data Component
    COMPONENTS.register("crafted_timestamp", () -> DataComponentRegistry.CRAFTED_TIMESTAMP);
    COMPONENTS.register("crafted_by", () -> DataComponentRegistry.CRAFTED_BY);
    COMPONENTS.register("enchanted_timestamp", () -> DataComponentRegistry.ENCHANTED_TIMESTAMP);
    COMPONENTS.register("enchanted_by", () -> DataComponentRegistry.ENCHANTED_BY);
    COMPONENTS.register(eventBus);
  }

  private static final DeferredRegister<DataComponentType<?>> COMPONENTS =
          DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);
}