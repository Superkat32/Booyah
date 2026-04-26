package net.superkat.booyah.network.packets.booyah;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.superkat.booyah.Booyah;

public record CommonBooyahPacket(int playerId) implements CustomPacketPayload {
    public static final Identifier ID = Booyah.id("common_booyah_packet");
    public static final CustomPacketPayload.Type<CommonBooyahPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, CommonBooyahPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CommonBooyahPacket::playerId,
            CommonBooyahPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
