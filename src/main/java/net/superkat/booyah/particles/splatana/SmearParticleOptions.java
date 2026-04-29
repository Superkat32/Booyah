package net.superkat.booyah.particles.splatana;

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

public record SmearParticleOptions(int color, boolean reversed, float rotX, float rotY) implements ParticleOptions {

    public static final MapCodec<SmearParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(options -> options.color),
                    Codec.BOOL.fieldOf("reversed").forGetter(options -> options.reversed),
                    Codec.FLOAT.fieldOf("rotx").forGetter(options -> options.rotX),
                    Codec.FLOAT.fieldOf("roty").forGetter(options -> options.rotY)
            ).apply(instance, SmearParticleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SmearParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, options -> options.color,
            ByteBufCodecs.BOOL, options -> options.reversed,
            ByteBufCodecs.FLOAT, options -> options.rotX,
            ByteBufCodecs.FLOAT, options -> options.rotY,
            SmearParticleOptions::new
    );

    @Override
    public @NonNull ParticleType<?> getType() {
        return BooyahParticles.SMEAR;
    }
}
