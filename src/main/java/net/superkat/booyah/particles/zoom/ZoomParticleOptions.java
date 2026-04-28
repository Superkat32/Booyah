package net.superkat.booyah.particles.zoom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.superkat.booyah.particles.BooyahParticles;

public record ZoomParticleOptions(int color, float yaw, float pitch, float stretch) implements ParticleOptions {
    public static final MapCodec<ZoomParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(particle -> particle.color),
                    Codec.FLOAT.fieldOf("yaw").forGetter(particle -> particle.yaw),
                    Codec.FLOAT.fieldOf("pitch").forGetter(particle -> particle.pitch),
                    Codec.FLOAT.fieldOf("stretch").forGetter(particle -> particle.stretch)
            ).apply(instance, ZoomParticleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ZoomParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ZoomParticleOptions::color,
            ByteBufCodecs.FLOAT, ZoomParticleOptions::yaw,
            ByteBufCodecs.FLOAT, ZoomParticleOptions::pitch,
            ByteBufCodecs.FLOAT, ZoomParticleOptions::stretch,
            ZoomParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return BooyahParticles.ZOOM;
    }
}
