package com.khazoda.heirlooms;


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
    COMPONENTS.register("crafted_timestamp", () -> HeirloomsMod.CRAFTED_TIMESTAMP);
    COMPONENTS.register(eventBus);
  }

  private static final DeferredRegister<DataComponentType<?>> COMPONENTS =
          DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

}