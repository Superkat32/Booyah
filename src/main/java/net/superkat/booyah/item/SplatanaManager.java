package net.superkat.booyah.item;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.item.color.SplatanaColorSet;
import net.superkat.booyah.item.color.SplatanaColors;
import net.superkat.booyah.network.packets.S2CSplatanaSwingPacket;
import net.superkat.booyah.particles.smear.SmearEmitterParticleOptions;

public class SplatanaManager {

    public static void onSplatanaPlayerSwing(LivingEntity player) {
        // Only activate if original arm is swinging
        if (!player.swinging || !(player instanceof SplatanaPlayer splatanaPlayer)) return;

        if (!BooyahItems.isSplatana(player.getMainHandItem())) return;

        if (splatanaPlayer.booyah$isSplatanaSwinging()) {
            splatanaPlayer.booyah$setQueuedReverseUpdate(!splatanaPlayer.booyah$reverseSplatanaSwing());
        }
        splatanaPlayer.booyah$setIsSplatanaSwinging(true);
        splatanaPlayer.booyah$setSplatanaSwingTime(-1); // Start at -1 for buffer? Vanilla does it so ¯\_(ツ)_/¯

        if (player.level() instanceof ServerLevel) {
            S2CSplatanaSwingPacket swingPacket = new S2CSplatanaSwingPacket(player.getId(), -1, splatanaPlayer.booyah$queuedReverseUpdate());
            for (ServerPlayer serverPlayer : PlayerLookup.tracking(player)) {
                if (serverPlayer == player) continue;
                ServerPlayNetworking.send(serverPlayer, swingPacket);
            }
        } else {
            spawnSplatanaSwingParticles(player.level(), player);
        }
    }

    public static void updateSplatanaPlayer(LivingEntity player, int currentSwingDuration) {
        if (!(player instanceof SplatanaPlayer splatanaPlayer)) return;

        // Extend splatana swing beyond normal swing animation
        int swingTime = splatanaPlayer.booyah$getSplatanaSwingTime();
        int maxSwingTime = currentSwingDuration > 0 ? currentSwingDuration + 10 : 0;
        if (splatanaPlayer.booyah$isSplatanaSwinging()) {
            swingTime++;
            if (swingTime == 1) {
                if (splatanaPlayer.booyah$queuedReverseUpdate() != splatanaPlayer.booyah$reverseSplatanaSwing()) {
                    splatanaPlayer.booyah$setReverseSplatanaSwing(splatanaPlayer.booyah$queuedReverseUpdate());
                }
            }
            if (swingTime >= maxSwingTime) {
                swingTime = 0;
                splatanaPlayer.booyah$setIsSplatanaSwinging(false);
            }
        } else {
            splatanaPlayer.booyah$setReverseSplatanaSwing(false);
            splatanaPlayer.booyah$setQueuedReverseUpdate(false);
            swingTime = 0;
        }

        splatanaPlayer.booyah$setSplatanaSwingTime(swingTime);
        splatanaPlayer.booyah$setSplatanaAttackAnim((float) swingTime / maxSwingTime);
    }

    public static void spawnSplatanaSwingParticles(Level level, LivingEntity player) {
        if (!player.level().isClientSide() || !(player instanceof SplatanaPlayer splatanaPlayer)) return;
        double dx = -Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)) * 0.8f;
        double dz = Mth.cos(player.getYRot() * (float) (Math.PI / 180.0)) * 0.8f;

        SplatanaColorSet colorSet = SplatanaColors.getSplatanaColorSet(player.getMainHandItem());
//        int color = colorSet.getRandomColor(player.getRandom(), 0.5f);
//        int color = ARGB.colorFromFloat(1f, 0.71f, 0.37f, 0.92f);
        boolean reversed = splatanaPlayer.booyah$queuedReverseUpdate();
        level.addParticle(new SmearEmitterParticleOptions(1, 1, 16, colorSet, reversed), player.getX() + dx, player.getY(0.4), player.getZ() + dz, dx, 0, dz);
    }
}
