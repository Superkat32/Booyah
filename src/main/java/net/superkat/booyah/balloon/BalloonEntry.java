package net.superkat.booyah.balloon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BalloonEntry(BlockPos pos, int index) {

    public static final Codec<BalloonEntry> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(entry -> entry.pos),
                    Codec.INT.fieldOf("index").forGetter(entry -> entry.index)
            ).apply(instance, BalloonEntry::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BalloonEntry> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BalloonEntry::pos,
            ByteBufCodecs.INT, BalloonEntry::index,
            BalloonEntry::new
    );

}
