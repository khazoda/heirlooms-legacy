package com.khazoda.heirlooms;

import net.fabricmc.api.ModInitializer;

public class HeirloomsFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        HeirloomsCommon.init();
    }
}
