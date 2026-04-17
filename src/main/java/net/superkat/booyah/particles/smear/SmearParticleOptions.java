package net.superkat.booyah.particles.smear;

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
import org.jspecify.annotations.NonNull;

public record SmearParticleOptions(int color, boolean reversed) implements ParticleOptions {

    public static final MapCodec<SmearParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(options -> options.color),
                    Codec.BOOL.fieldOf("reversed").forGetter(options -> options.reversed)
            ).apply(instance, SmearParticleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SmearParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, options -> options.color,
            ByteBufCodecs.BOOL, options -> options.reversed,
            SmearParticleOptions::new
    );

    @Override
    public @NonNull ParticleType<?> getType() {
        return BooyahParticles.SMEAR;
    }
}
