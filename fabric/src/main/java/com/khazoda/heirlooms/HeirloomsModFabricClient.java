package com.khazoda.heirlooms;

import net.fabricmc.api.ClientModInitializer;


public class HeirloomsModFabricClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    TooltipHandler.register();
  }
}
