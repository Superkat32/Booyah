package net.superkat.booyah.particles.balloon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;

public class BalloonPopParticle extends SingleQuadParticle {
    public float rollPerTick;
    public BalloonPopParticle(ClientLevel level, double x, double y, double z, double velX, double velY, double velZ, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        this.xd = velX;
        this.yd = velY;
        this.zd = velZ;
        this.gravity = 0.75f;
        this.roll = (float) Math.toRadians(this.random.nextInt(360));
        this.oRoll = this.roll;
        this.rollPerTick = (float) Math.toRadians(this.random.nextFloat() * 75);
        this.lifetime = 60;

        this.setSprite(sprites.get(this.random));
    }

    @Override
    public void tick() {
        this.oRoll = this.roll;
        this.roll += this.rollPerTick;
        if (this.onGround) {
            this.roll = 0f;
            this.alpha -= 0.1f;
        }
        if (this.alpha <= 0) {
            this.remove();
        }
        super.tick();
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.TRANSLUCENT;
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
            return new BalloonPopParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
        }
    }
}
