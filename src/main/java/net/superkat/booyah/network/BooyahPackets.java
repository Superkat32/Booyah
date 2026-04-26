package net.superkat.booyah.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.superkat.booyah.network.packets.balloon.C2SBalloonChaseBlockUpdatePacket;
import net.superkat.booyah.network.packets.booyah.CommonBooyahPacket;
import net.superkat.booyah.network.packets.splatana.S2CSplatanaSwingPacket;

public class BooyahPackets {

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(CommonBooyahPacket.TYPE, CommonBooyahPacket.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(CommonBooyahPacket.TYPE, CommonBooyahPacket.CODEC);

        PayloadTypeRegistry.clientboundPlay().register(S2CSplatanaSwingPacket.TYPE, S2CSplatanaSwingPacket.CODEC);

        PayloadTypeRegistry.serverboundPlay().register(C2SBalloonChaseBlockUpdatePacket.TYPE, C2SBalloonChaseBlockUpdatePacket.CODEC);
    }

}
