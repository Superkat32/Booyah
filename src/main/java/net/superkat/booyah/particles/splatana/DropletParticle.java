package net.superkat.booyah.particles.splatana;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;

public class DropletParticle extends SingleQuadParticle {
    public DropletParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, DropletParticleOptions options, SpriteSet spriteSet) {
        super(level, x, y, z, xa, ya, za, spriteSet.first());
        this.rCol = ARGB.redFloat(options.color());
        this.gCol = ARGB.greenFloat(options.color());
        this.bCol = ARGB.blueFloat(options.color());
        this.xd = xa;
        this.yd = ya;
        this.zd = za;
        this.gravity = 0.06f;
    }

    @Override
    public void tick() {
        this.yd -= this.gravity;
        super.tick();
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet spriteSet) implements ParticleProvider<DropletParticleOptions> {
        @Override
        public @NonNull Particle createParticle(DropletParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new DropletParticle(level, x, y, z, xAux, yAux, zAux, options, spriteSet);
        }
    }
}
