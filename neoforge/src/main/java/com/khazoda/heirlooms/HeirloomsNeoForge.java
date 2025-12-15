package com.khazoda.heirlooms;


import com.khazoda.heirlooms.registry.DataComponentRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class HeirloomsNeoForge {

  private static final DeferredRegister<DataComponentType<?>> COMPONENTS =
          DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

  public HeirloomsNeoForge(IEventBus eventBus) {

    // Register Data Component
    COMPONENTS.register("crafted_timestamp", () -> DataComponentRegistry.CRAFTED_TIMESTAMP);
    COMPONENTS.register("crafted_by", () -> DataComponentRegistry.CRAFTED_BY);
    COMPONENTS.register("enchanted_timestamp", () -> DataComponentRegistry.ENCHANTED_TIMESTAMP);
    COMPONENTS.register("enchanted_by", () -> DataComponentRegistry.ENCHANTED_BY);
    COMPONENTS.register(eventBus);

    HeirloomsCommon.init();
    eventBus.addListener(this::onRegister);
  }

  private void onRegister(RegisterEvent event) {
    HeirloomsCommon.REGISTRARS.register(event.getRegistry());
  }
}