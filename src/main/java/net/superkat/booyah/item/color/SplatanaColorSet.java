package net.superkat.booyah.item.color;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.color.HSVColor;

import java.util.List;

public record SplatanaColorSet(List<HSVColor> colors, Vec3 colorVariation) {
    public static final Codec<SplatanaColorSet> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    HSVColor.CODEC.listOf().fieldOf("colors").forGetter(SplatanaColorSet::colors),
                    Vec3.CODEC.fieldOf("variation").forGetter(SplatanaColorSet::colorVariation)
            ).apply(instance, SplatanaColorSet::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SplatanaColorSet> STREAM_CODEC = StreamCodec.composite(
            HSVColor.STREAM_CODEC.apply(ByteBufCodecs.list()), SplatanaColorSet::colors,
            Vec3.STREAM_CODEC, SplatanaColorSet::colorVariation,
            SplatanaColorSet::new
    );

    public SplatanaColorSet(HSVColor color, Vec3 colorVariation) {
        this(List.of(color), colorVariation);
    }

    public SplatanaColorSet(HSVColor color, Vec3i colorVariation) {
        this(List.of(color), new Vec3(colorVariation.getX() / 360f, colorVariation.getY() / 100f, colorVariation.getZ() / 100f));
    }

    public int getMainColor() {
        if (this.colors.isEmpty()) return CommonColors.WHITE;
        return this.colors.getFirst().getARGB();
    }

    public int getRandomColor(RandomSource random, float chanceOfVariation) {
        HSVColor baseColor = this.colors.get(random.nextInt(this.colors.size()));
        if (random.nextFloat() <= chanceOfVariation) {
            float randomHue = (float) (this.colorVariation.x() * randomFloat(random));
            float randomSaturation = (float) (this.colorVariation.y() * randomFloat(random));
            float randomValue = (float) (this.colorVariation.z() * randomFloat(random));
            float hue = Mth.clamp(baseColor.hue() + randomHue, 0f, 1f);
            float saturation = Mth.clamp(baseColor.saturation() + randomSaturation, 0f, 1f);
            float value = Mth.clamp(baseColor.value() + randomValue, 0f, 1f);
            baseColor = new HSVColor(hue, saturation, value);
        }

        return baseColor.getARGB();
    }

    private float randomFloat(RandomSource random) {
        return random.nextFloat() * 2f - 1f;
    }
}
