package com.khazoda.heirlooms;

import com.khazoda.heirlooms.block.DisplayRackBlockEntity;
import com.khazoda.heirlooms.block.renderer.DisplayCaseRenderer;
import com.khazoda.heirlooms.block.renderer.DisplayRackRenderer;
import com.khazoda.heirlooms.registry.MainRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class HeirloomsNeoForgeClient {

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(MainRegistry.DISPLAY_CASE_BE.get(), DisplayCaseRenderer::new);
    event.registerBlockEntityRenderer(MainRegistry.DISPLAY_RACK_BE.get(), DisplayRackRenderer::new);

  }

  public HeirloomsNeoForgeClient(IEventBus eventBus) {
    eventBus.addListener(this::onClientSetup);
  }

  public void onClientSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      ItemBlockRenderTypes.setRenderLayer(MainRegistry.DISPLAY_CASE.get(), ChunkSectionLayer.CUTOUT);
      ItemBlockRenderTypes.setRenderLayer(MainRegistry.DISPLAY_RACK.get(), ChunkSectionLayer.CUTOUT);

    });
  }
}