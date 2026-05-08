package net.superkat.booyah.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.superkat.booyah.balloon.BalloonChainManager;
import net.superkat.booyah.block.BalloonChaseBlock;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.network.packets.balloon.C2SBalloonChaseBlockUpdatePacket;
import net.superkat.booyah.network.packets.booyah.CommonBooyahPacket;

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
        BlockPos pos = payload.entry().pos();

        if (payload.chainId().isBlank()) {
            context.player().sendSystemMessage(Component.literal("Chain ID is blank!").withStyle(ChatFormatting.RED));
            return;
        }

        if (level.getBlockEntity(pos) instanceof BalloonChaseBlockEntity balloonChaseBlock) {
            BalloonChainManager chainManager = BalloonChainManager.get(level);
            MutableComponent message = Component.literal("Saved Balloon - ");
            Component chainIdName = Component.literal(payload.chainId()).withStyle(ChatFormatting.AQUA);
            Component chainIndex = Component.literal(String.valueOf(payload.entry().index()))
                    .withColor(BalloonChaseBlock.getRandomColorForIndex(payload.entry().index()).getARGB());


            if (chainManager.hasChain(payload.chainId())) {
                boolean updating = chainManager.getOrCreateChain(payload.chainId()).entries().containsKey(payload.entry().pos());
                message.append(
                        Component.literal(updating ? "Updated " : "Added to ")
                                .append(chainIdName)
                                .append(" ").append(chainIndex)
                                .append("!")
                );
            } else {
                message.append(
                        Component.literal("Created ")
                                .append(chainIdName)
                                .append("!")
                );
            }
            context.player().sendSystemMessage(message);

            balloonChaseBlock.updateBalloonEntry(payload.chainId(), payload.entry());
        }
    }
}
