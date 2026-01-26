package com.khazoda.heirlooms;

import com.khazoda.heirlooms.platform.Services;

public class HeirloomsCommon {

  public static final String CRAFTED_TIMESTAMP = "heirlooms:crafted_timestamp";
  public static final String CRAFTED_BY = "heirlooms:crafted_by";
  public static final String ENCHANTED_TIMESTAMP = "heirlooms:enchanted_timestamp";
  public static final String ENCHANTED_BY = "heirlooms:enchanted_by";

  public static void init() {
    if (Services.PLATFORM.isModLoaded("heirlooms")) Constants.LOG.info("- Heirlooms Loaded -");
  }
}