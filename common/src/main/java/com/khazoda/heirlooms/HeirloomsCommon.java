package com.khazoda.heirlooms;

import com.khazoda.heirlooms.platform.Services;
import com.khazoda.heirlooms.registry.MainRegistry;
import com.khazoda.heirlooms.registry.TabRegistry;
import com.khazoda.heirlooms.registry.helper.Reginald;


public class HeirloomsCommon {
  public static final Reginald REGISTRARS = new Reginald();

  public static void init() {
    MainRegistry.init();
    TabRegistry.init();
    if (Services.PLATFORM.isModLoaded("heirlooms")) Constants.LOG.info("- Heirlooms Loaded -");
  }
}