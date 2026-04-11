package net.superkat.booyah.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.superkat.booyah.comm.BooyahClientManager;
import net.superkat.booyah.network.packets.CommonBooyahPacket;

public class BooyahClientNetworkHandler {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(CommonBooyahPacket.TYPE, BooyahClientNetworkHandler::onPlayerBooyah);
    }

    public static void onPlayerBooyah(CommonBooyahPacket payload, ClientPlayNetworking.Context context) {
        Level level = context.player().level();
        int playerId = payload.playerId();

        Entity entity = level.getEntity(playerId);
        if (!(entity instanceof Player player)) return;

        BooyahClientManager.onPlayerBooyah(player);
    }

}
