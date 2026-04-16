package net.superkat.booyah.particles.smear;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.superkat.booyah.particles.BooyahParticles;
import org.jspecify.annotations.NonNull;

public record SmearEmitterParticleOptions(int time, int delay, int count, int color, boolean reversed) implements ParticleOptions {

    public static final MapCodec<SmearEmitterParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("time").forGetter(options -> options.time),
                    Codec.INT.fieldOf("delay").forGetter(options -> options.delay),
                    Codec.INT.fieldOf("count").forGetter(options -> options.count),
                    Codec.INT.fieldOf("color").forGetter(options -> options.color),
                    Codec.BOOL.fieldOf("reversed").forGetter(options -> options.reversed)
            ).apply(instance, SmearEmitterParticleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SmearEmitterParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SmearEmitterParticleOptions::time,
            ByteBufCodecs.INT, SmearEmitterParticleOptions::delay,
            ByteBufCodecs.INT, SmearEmitterParticleOptions::count,
            ByteBufCodecs.INT, SmearEmitterParticleOptions::color,
            ByteBufCodecs.BOOL, SmearEmitterParticleOptions::reversed,
            SmearEmitterParticleOptions::new
    );

    @Override
    public @NonNull ParticleType<?> getType() {
        return BooyahParticles.SMEAR_EMITTER;
    }
}
