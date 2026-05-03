package net.superkat.booyah.item;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.entity.SplatanaSwipe;
import net.superkat.booyah.item.color.SplatanaColorSet;
import net.superkat.booyah.item.color.SplatanaColors;
import net.superkat.booyah.network.packets.splatana.S2CSplatanaSwingPacket;
import net.superkat.booyah.particles.splatana.SmearEmitterParticleOptions;

import java.util.List;

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
        splatanaPlayer.booyah$setSplatanaSlashTime(0);
        splatanaPlayer.booyah$setMaxSplatanaSlashTime(-1);

        if (player.level() instanceof ServerLevel serverLevel) {
            splatanaPlayer.booyah$setMaxSplatanaHitboxTicks(6);
            splatanaPlayer.booyah$setSplatanaHitboxTicks(8);
            SplatanaColorSet colorSet = SplatanaColors.getSplatanaColorSet(player.getMainHandItem());
            SplatanaSwipe swipe = Projectile.spawnProjectileFromRotation(SplatanaSwipe::new, serverLevel, player.getMainHandItem(), player, 0, 1.25F, 0);
            swipe.setPos(swipe.position().add(0, -0.15, 0));

            int mainColor = colorSet.getRandomColor(player.getRandom(), 0);
            swipe.getEntityData().set(SplatanaSwipe.COLOR_ID, mainColor);
            int altColorA = colorSet.getRandomColor(player.getRandom(), 0.75f);
            swipe.getEntityData().set(SplatanaSwipe.ALT_COLOR_A_ID, altColorA);
            int altColorB = colorSet.getRandomColor(player.getRandom(), 0.75f);
            swipe.getEntityData().set(SplatanaSwipe.ALT_COLOR_B_ID, altColorB);
            int altColorC = colorSet.getRandomColor(player.getRandom(), 0.75f);
            swipe.getEntityData().set(SplatanaSwipe.ALT_COLOR_C_ID, altColorC);

            swipe.setOwner(player);

            S2CSplatanaSwingPacket swingPacket = new S2CSplatanaSwingPacket(player.getId(), -1, splatanaPlayer.booyah$queuedReverseUpdate());
            for (ServerPlayer serverPlayer : PlayerLookup.tracking(player)) {
                if (serverPlayer == player) continue;
                ServerPlayNetworking.send(serverPlayer, swingPacket);
            }

            serverLevel.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.5f, 1f);
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

        int slashTime = splatanaPlayer.booyah$splatanaSlashTime();
        int maxSlashTime = splatanaPlayer.booyah$maxSplatanaSlashTime();
        if (maxSlashTime > 0) {
            slashTime++;

            if (slashTime >= maxSlashTime) {
                slashTime = 0;
                splatanaPlayer.booyah$setMaxSplatanaSlashTime(-1);
            }
        }
        splatanaPlayer.booyah$setSplatanaSlashTime(slashTime);
        splatanaPlayer.booyah$setSplatanaSlashAnim(maxSlashTime == 0 ? 0 : (float) slashTime / maxSlashTime);
    }

    public static void spawnSplatanaSwingParticles(Level level, LivingEntity player) {
        if (!player.level().isClientSide() || !(player instanceof SplatanaPlayer splatanaPlayer)) return;
        float distanceFromPlayer = 0.75f;
        double dx = -Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)) * distanceFromPlayer;
        double dz = Mth.cos(player.getYRot() * (float) (Math.PI / 180.0)) * distanceFromPlayer;

        SplatanaColorSet colorSet = SplatanaColors.getSplatanaColorSet(player.getMainHandItem());
        boolean reversed = splatanaPlayer.booyah$queuedReverseUpdate();
        level.addParticle(new SmearEmitterParticleOptions(1, 2, 16, colorSet, reversed, 0, player.getYRot()), player.getX() + dx, player.getY(0.5), player.getZ() + dz, dx, 0, dz);
    }

    public static void aiStepSplatanaPlayer(LivingEntity player) {
        if (player.level().isClientSide()) return;
        if (!(player instanceof SplatanaPlayer splatanaPlayer)) return;
        if (!(player instanceof Player player1)) return;

        int maxSplatanaHitboxTicks = splatanaPlayer.booyah$maxSplatanaHitboxTicks();
        int splatanaHitboxTicks = splatanaPlayer.booyah$splatanaHitboxTicks();
        if (maxSplatanaHitboxTicks <= 0) return;

        splatanaHitboxTicks--;
        splatanaPlayer.booyah$setSplatanaHitboxTicks(splatanaHitboxTicks);
        if (splatanaHitboxTicks <= 0) {
            splatanaPlayer.booyah$setMaxSplatanaHitboxTicks(0);
            splatanaPlayer.booyah$setSplatanaHitboxTicks(0);
        } else if(splatanaHitboxTicks <= maxSplatanaHitboxTicks) {
            AABB hitbox = player.getBoundingBox().inflate(0.15f).inflate(0, 0.25f, 0);
            if (splatanaPlayer.booyah$isSplatanaSwinging()) hitbox.inflate(5f, 0f, 5f);
            List<Entity> hitEntities = player.level().getEntities(player, hitbox);
            if (hitEntities.isEmpty()) return;

            for (Entity hitEntity : hitEntities) {
                if (!(hitEntity instanceof LivingEntity entity)) continue;
                player1.attack(entity);
            }
        }
    }
}
