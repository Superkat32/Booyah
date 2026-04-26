package net.superkat.booyah.network.packets.splatana;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.superkat.booyah.Booyah;
import org.jspecify.annotations.NonNull;

public record S2CSplatanaSwingPacket(int playerId, int splatanaColor, boolean reversedSwing) implements CustomPacketPayload {
    public static final Identifier ID = Booyah.id("s2c_splatana_swing_packet");
    public static final Type<S2CSplatanaSwingPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSplatanaSwingPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, S2CSplatanaSwingPacket::playerId,
            ByteBufCodecs.INT, S2CSplatanaSwingPacket::splatanaColor,
            ByteBufCodecs.BOOL, S2CSplatanaSwingPacket::reversedSwing,
            S2CSplatanaSwingPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
