package net.superkat.booyah.color;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;

// Alternative to java.awt.Color because that causes some issues on specific Macs
public record HSVColor(float hue, float saturation, float value) {
    public static final Codec<HSVColor> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("hue").forGetter(HSVColor::hue),
                    Codec.FLOAT.fieldOf("saturation").forGetter(HSVColor::saturation),
                    Codec.FLOAT.fieldOf("value").forGetter(HSVColor::value)
            ).apply(instance, HSVColor::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, HSVColor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, HSVColor::hue,
            ByteBufCodecs.FLOAT, HSVColor::saturation,
            ByteBufCodecs.FLOAT, HSVColor::value,
            HSVColor::new
    );

    public HSVColor(int hue, int saturation, int value) {
        this(hue / 360f, saturation / 100f, value / 100f);
    }

    public int getARGB() {
        return getARGB(1f);
    }

    public int getARGB(float alpha) {
        int rgb = HSBtoRGB(this.hue, this.saturation, this.value);
        return ARGB.color(alpha, rgb);
    }

    // Copy-pasted from java.awt.Color
    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
    }
}
