package net.superkat.booyah.item.client;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.item.SplatanaManager;
import net.superkat.booyah.item.color.SplatanaColorSet;
import net.superkat.booyah.particles.splatana.SmearEmitterParticleOptions;

public class SplatanaClientManager {

    public static void onPlayerSplatanaSwing(Level level, Player player, int splatanaColor, boolean reversed) {
        SplatanaPlayer splatanaPlayer = (SplatanaPlayer) player;
        splatanaPlayer.booyah$setIsSplatanaSwinging(true);
        splatanaPlayer.booyah$setSplatanaSwingTime(-1); // Start at -1 for buffer? Vanilla does it so ¯\_(ツ)_/¯
        splatanaPlayer.booyah$setQueuedReverseUpdate(reversed);

        SplatanaManager.spawnSplatanaSwingParticles(level, player);
    }

    public static void onPlayerSplatanaSlash(Level level, Player player, SplatanaColorSet colorSet) {
        SplatanaPlayer splatanaPlayer = (SplatanaPlayer) player;

        splatanaPlayer.booyah$setSplatanaSlashTime(0);
        splatanaPlayer.booyah$setMaxSplatanaSlashTime(28);

        float distanceFromPlayer = 3f;
        double dx = -Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)) * distanceFromPlayer;
        double dz = Mth.cos(player.getYRot() * (float) (Math.PI / 180.0)) * distanceFromPlayer;
        level.addParticle(new SmearEmitterParticleOptions(2, 2, 16, colorSet, false, -90, player.getYRot()), player.getX() + dx, player.getY(0.5), player.getZ() + dz, dx, 0, dz);
    }
}
