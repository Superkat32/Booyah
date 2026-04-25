package net.superkat.booyah.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.network.packets.CommonBooyahPacket;
import net.superkat.booyah.network.packets.balloon.C2SBalloonChaseBlockUpdatePacket;

public class BooyahServerNetworkHandler {

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(CommonBooyahPacket.TYPE, BooyahServerNetworkHandler::onPlayerBooyah);

        ServerPlayNetworking.registerGlobalReceiver(C2SBalloonChaseBlockUpdatePacket.TYPE, BooyahServerNetworkHandler::onBalloonChaseBlockUpdate);
    }

    public static void onPlayerBooyah(CommonBooyahPacket payload, ServerPlayNetworking.Context context) {
        ServerLevel level = context.player().level();
        int playerId = payload.playerId();

        Entity entity = level.getEntity(playerId);
        if (!(entity instanceof Player player)) return;
        for (ServerPlayer trackingPlayer : PlayerLookup.tracking(player)) {
            if (trackingPlayer == player) continue;
            ServerPlayNetworking.send(trackingPlayer, new CommonBooyahPacket(player.getId()));
        }
    }

    public static void onBalloonChaseBlockUpdate(C2SBalloonChaseBlockUpdatePacket payload, ServerPlayNetworking.Context context) {
        ServerLevel level = context.player().level();
        BlockPos pos = payload.pos();

        if (level.getBlockEntity(pos) instanceof BalloonChaseBlockEntity balloonChaseBlock) {
            balloonChaseBlock.updateBalloonEntry(payload.chainId(), payload.entryIndex());
        }
    }
}
