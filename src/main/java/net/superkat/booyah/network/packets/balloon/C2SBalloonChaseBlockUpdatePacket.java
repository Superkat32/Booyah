package net.superkat.booyah.network.packets.balloon;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.balloon.BalloonEntry;

public record C2SBalloonChaseBlockUpdatePacket(String chainId, BalloonEntry entry) implements CustomPacketPayload {
    public static final Identifier ID = Booyah.id("c2s_balloon_chase_block_update_packet");
    public static final Type<C2SBalloonChaseBlockUpdatePacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SBalloonChaseBlockUpdatePacket> CODEC = StreamCodec.composite(
//            BlockPos.STREAM_CODEC, C2SBalloonChaseBlockUpdatePacket::pos,
            ByteBufCodecs.STRING_UTF8, C2SBalloonChaseBlockUpdatePacket::chainId,
            BalloonEntry.STREAM_CODEC, C2SBalloonChaseBlockUpdatePacket::entry,
            C2SBalloonChaseBlockUpdatePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
