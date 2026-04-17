package net.superkat.booyah.item.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.item.SplatanaManager;

public class SplatanaClientManager {

    public static void onPlayerSplatanaSwing(Level level, Player player, int splatanaColor, boolean reversed) {
        SplatanaPlayer splatanaPlayer = (SplatanaPlayer) player;
        splatanaPlayer.booyah$setIsSplatanaSwinging(true);
        splatanaPlayer.booyah$setSplatanaSwingTime(-1); // Start at -1 for buffer? Vanilla does it so ¯\_(ツ)_/¯
        splatanaPlayer.booyah$setQueuedReverseUpdate(reversed);

        SplatanaManager.spawnSplatanaSwingParticles(level, player);
    }

}
