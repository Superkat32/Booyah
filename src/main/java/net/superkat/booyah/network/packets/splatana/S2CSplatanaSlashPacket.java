package net.superkat.booyah.network.packets.splatana;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.item.color.SplatanaColorSet;

public record S2CSplatanaSlashPacket(int playerId, SplatanaColorSet colorSet) implements CustomPacketPayload {
    public static final Identifier ID = Booyah.id("s2c_splatana_slash_packet");
    public static final Type<S2CSplatanaSlashPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSplatanaSlashPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, S2CSplatanaSlashPacket::playerId,
            SplatanaColorSet.STREAM_CODEC, S2CSplatanaSlashPacket::colorSet,
            S2CSplatanaSlashPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
