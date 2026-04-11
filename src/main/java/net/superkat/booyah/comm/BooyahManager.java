package net.superkat.booyah.comm;

import net.minecraft.world.entity.player.Player;
import net.superkat.booyah.duck.BooyahablePlayer;

public class BooyahManager {

    public static void tickBooyahablePlayer(Player player) {
        BooyahablePlayer booyahablePlayer = (BooyahablePlayer) player;
        int booyahTicks = booyahablePlayer.booyah$booyahTicks();
        if (booyahTicks > 0) {
            booyahTicks--;
            booyahablePlayer.booyah$setBooyahTicks(booyahTicks);
        }
    }

}
