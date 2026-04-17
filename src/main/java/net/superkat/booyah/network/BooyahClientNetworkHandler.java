package net.superkat.booyah.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.superkat.booyah.comm.BooyahClientManager;
import net.superkat.booyah.item.client.SplatanaClientManager;
import net.superkat.booyah.network.packets.CommonBooyahPacket;
import net.superkat.booyah.network.packets.S2CSplatanaSwingPacket;

public class BooyahClientNetworkHandler {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(CommonBooyahPacket.TYPE, BooyahClientNetworkHandler::onPlayerBooyah);

        ClientPlayNetworking.registerGlobalReceiver(S2CSplatanaSwingPacket.TYPE, BooyahClientNetworkHandler::onPlayerSplatanaSwing);
    }

    public static void onPlayerBooyah(CommonBooyahPacket payload, ClientPlayNetworking.Context context) {
        Level level = context.player().level();
        int playerId = payload.playerId();

        Entity entity = level.getEntity(playerId);
        if (!(entity instanceof Player player)) return;

        BooyahClientManager.onPlayerBooyah(player);
    }

    public static void onPlayerSplatanaSwing(S2CSplatanaSwingPacket payload, ClientPlayNetworking.Context context) {
        Level level = context.player().level();
        int playerId = payload.playerId();

        Entity entity = level.getEntity(playerId);
        if (!(entity instanceof Player player)) return;

        SplatanaClientManager.onPlayerSplatanaSwing(level, player, payload.splatanaColor(), payload.reversedSwing());
    }

}
