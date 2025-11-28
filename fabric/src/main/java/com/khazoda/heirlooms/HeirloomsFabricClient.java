package com.khazoda.heirlooms;

import com.khazoda.heirlooms.block.renderer.DisplayCaseRenderer;
import com.khazoda.heirlooms.registry.MainRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;


public class HeirloomsFabricClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    TooltipHandler.register();
    BlockEntityRenderers.register(MainRegistry.DISPLAY_CASE_BE.get(), DisplayCaseRenderer::new);
    BlockRenderLayerMap.putBlock(MainRegistry.DISPLAY_CASE.get(), ChunkSectionLayer.CUTOUT);
  }
}
