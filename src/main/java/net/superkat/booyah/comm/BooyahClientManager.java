package net.superkat.booyah.comm;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.superkat.booyah.duck.BooyahablePlayer;
import net.superkat.booyah.duck.LocalBooyahablePlayer;
import net.superkat.booyah.network.packets.CommonBooyahPacket;

@Environment(EnvType.CLIENT)
public class BooyahClientManager {
    public static final int SNEAKS_UNTIL_BOOYAH = 2;
    public static final int FURTHER_SNEAKS_UNTIL_BOOYAH = 4;
    public static final int TICKS_BETWEEN_SNEAKS = 15;

    public static void aiStepLocalPlayer(LocalPlayer player) {
        LocalBooyahablePlayer booyahablePlayer = (LocalBooyahablePlayer) player;
        booyahablePlayer.booyah$subtractSneakTicks();

        boolean prevSneak = booyahablePlayer.booyah$getWasSneaking();
        boolean currentSneak = player.isShiftKeyDown();
        booyahablePlayer.booyah$setWasSneaking(currentSneak);
        if (!prevSneak && currentSneak) {
            if (booyahablePlayer.booyah$sneakTicks() >= 0) {
                booyahablePlayer.booyah$setSneaksUntilBooyah(booyahablePlayer.booyah$getSneaksUntilBooyah() - 1);
                if (booyahablePlayer.booyah$getSneaksUntilBooyah() <= 0) {
                    sendBooyah(player);
                    booyahablePlayer.booyah$resetSneaksUntilBooyah(true);
                }
            }
            booyahablePlayer.booyah$resetSneakTicks();
        }
    }

    public static void sendBooyah(LocalPlayer player) {
        ClientPlayNetworking.send(new CommonBooyahPacket(player.getId()));
        onPlayerBooyah(player);
    }

    public static void onPlayerBooyah(Player player) {
        BooyahablePlayer booyahablePlayer = (BooyahablePlayer) player;
        if (booyahablePlayer.booyah$booyahTicks() <= 0) {
            booyahablePlayer.booyah$setTickCountOfBooyah(player.tickCount);
        }
        booyahablePlayer.booyah$setBooyahTicks(60);

        Level level = player.level();
        if (player.isLocalPlayer()) {
            player.sendOverlayMessage(Component.literal("Booyah!"));
        }
        level.playPlayerSound(SoundEvents.ALLAY_THROW, SoundSource.PLAYERS, 1f, 2f);
        level.playPlayerSound(SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, SoundSource.PLAYERS, 0.15f, 2f);
        level.playPlayerSound(SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 0.75f, 1.25f);
    }

}
