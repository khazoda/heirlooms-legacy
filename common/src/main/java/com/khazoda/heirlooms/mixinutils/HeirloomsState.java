package com.khazoda.heirlooms.mixinutils;

import net.minecraft.world.entity.player.Player;

public class HeirloomsState {
    private static final ThreadLocal<Player> CAPTURING_PLAYER = new ThreadLocal<>();

    public static void setCapturing(Player player) {
        CAPTURING_PLAYER.set(player);
    }

    public static Player getCapturingPlayer() {
        return CAPTURING_PLAYER.get();
    }
}
