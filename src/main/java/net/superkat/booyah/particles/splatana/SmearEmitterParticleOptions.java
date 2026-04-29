package net.superkat.booyah.particles.splatana;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.superkat.booyah.item.color.SplatanaColorSet;
import net.superkat.booyah.particles.BooyahParticles;
import org.jspecify.annotations.NonNull;

public record SmearEmitterParticleOptions(int time, int delay, int count, SplatanaColorSet colorSet, boolean reversed, float rotX, float rotY) implements ParticleOptions {

    public static final MapCodec<SmearEmitterParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("time").forGetter(options -> options.time),
                    Codec.INT.fieldOf("delay").forGetter(options -> options.delay),
                    Codec.INT.fieldOf("count").forGetter(options -> options.count),
                    SplatanaColorSet.CODEC.fieldOf("color_set").forGetter(options -> options.colorSet),
                    Codec.BOOL.fieldOf("reversed").forGetter(options -> options.reversed),
                    Codec.FLOAT.fieldOf("rotx").forGetter(options -> options.rotX),
                    Codec.FLOAT.fieldOf("rotz").forGetter(options -> options.rotY)
            ).apply(instance, SmearEmitterParticleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SmearEmitterParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SmearEmitterParticleOptions::time,
            ByteBufCodecs.INT, SmearEmitterParticleOptions::delay,
            ByteBufCodecs.INT, SmearEmitterParticleOptions::count,
            SplatanaColorSet.STREAM_CODEC, SmearEmitterParticleOptions::colorSet,
            ByteBufCodecs.BOOL, SmearEmitterParticleOptions::reversed,
            ByteBufCodecs.FLOAT, SmearEmitterParticleOptions::rotX,
            ByteBufCodecs.FLOAT, SmearEmitterParticleOptions::rotY,
            SmearEmitterParticleOptions::new
    );

    @Override
    public @NonNull ParticleType<?> getType() {
        return BooyahParticles.SMEAR_EMITTER;
    }
}
