package net.superkat.booyah.particles.splatana;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.superkat.booyah.particles.BooyahParticles;

public record DropletParticleOptions(int color) implements ParticleOptions {
    public static final MapCodec<DropletParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(particle -> particle.color)
            ).apply(instance, DropletParticleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DropletParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, DropletParticleOptions::color,
            DropletParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return BooyahParticles.DROPLET;
    }
}
