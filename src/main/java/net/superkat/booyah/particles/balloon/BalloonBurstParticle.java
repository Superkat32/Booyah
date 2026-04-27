package net.superkat.booyah.particles.balloon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HugeExplosionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;

public class BalloonBurstParticle extends HugeExplosionParticle {
    public BalloonBurstParticle(ClientLevel level, double x, double y, double z, double size, SpriteSet sprites) {
        super(level, x, y, z, size, sprites);
        this.quadSize = 1f;
    }

    @Environment(EnvType.CLIENT)
    public static record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(
                final SimpleParticleType options,
                final @NonNull ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final @NonNull RandomSource random
        ) {
            return new BalloonBurstParticle(level, x, y, z, xAux, this.sprites);
        }
    }
}
