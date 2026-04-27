package net.superkat.booyah.balloon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BalloonEntry(BlockPos pos, int index, int spawnDelayTicks, int floatAwayTicks, float balloonYaw) {

    public static final Codec<BalloonEntry> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(entry -> entry.pos),
                    Codec.INT.fieldOf("index").forGetter(entry -> entry.index),
                    Codec.INT.fieldOf("spawn_delay_ticks").forGetter(entry -> entry.spawnDelayTicks),
                    Codec.INT.fieldOf("float_away_ticks").forGetter(entry -> entry.floatAwayTicks),
                    Codec.FLOAT.fieldOf("balloon_yaw").forGetter(entry -> entry.balloonYaw)
            ).apply(instance, BalloonEntry::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BalloonEntry> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BalloonEntry::pos,
            ByteBufCodecs.INT, BalloonEntry::index,
            ByteBufCodecs.INT, BalloonEntry::spawnDelayTicks,
            ByteBufCodecs.INT, BalloonEntry::floatAwayTicks,
            ByteBufCodecs.FLOAT, BalloonEntry::balloonYaw,
            BalloonEntry::new
    );

    public BalloonEntry createNext(BlockPos pos) {
        return new BalloonEntry(pos, this.index + 1, this.spawnDelayTicks, this.floatAwayTicks, this.balloonYaw);
    }

}
