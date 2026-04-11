package net.superkat.booyah.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.superkat.booyah.network.packets.CommonBooyahPacket;

public class BooyahPackets {

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(CommonBooyahPacket.TYPE, CommonBooyahPacket.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(CommonBooyahPacket.TYPE, CommonBooyahPacket.CODEC);
    }

}
