package net.superkat.booyah.particles.splatana;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.util.RandomSource;
import net.superkat.booyah.item.color.SplatanaColorSet;
import org.jspecify.annotations.NonNull;

public class SmearEmitterParticle extends NoRenderParticle {
    public int delay;
    public int count;
    public int countPerTick;
    public SplatanaColorSet colorSet;
    public boolean reversed;
    public float rotX;
    public float rotY;
    public SmearEmitterParticle(ClientLevel level, double x, double y, double z, SmearEmitterParticleOptions options) {
        super(level, x, y, z);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.delay = options.delay();
        int time = options.time();
        this.lifetime = time + this.delay;
        this.count = options.count();
        this.countPerTick = time <= 1 ? this.count : this.count / time;
        this.colorSet = options.colorSet();
        this.reversed = options.reversed();
        this.rotX = options.rotX();
        this.rotY = options.rotY();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age >= this.delay) {
            for (int i = 0; i < this.countPerTick; i++) {
                this.count--;
                float spreadXZ = 0.3f + (this.rotX == -90 ? this.random.nextFloat() * 0.8f - 0.4f : 0);
                float spreadY = 0.2f;
                float offsetX = this.random.nextFloat() * spreadXZ;
                float offsetY = this.random.nextFloat() * spreadY;
                float offsetZ = this.random.nextFloat() * spreadXZ;
                int color = colorSet.getRandomColor(this.random, 0.5f);
                this.level.addParticle(new SmearParticleOptions(color, this.reversed, this.rotX, this.rotY),
                        this.x + offsetX, this.y + offsetY, this.z + offsetZ, 0, 0, 0);
            }
        }

        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    public static class Provider implements ParticleProvider<SmearEmitterParticleOptions> {
        public Particle createParticle(
                final SmearEmitterParticleOptions options,
                final @NonNull ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final @NonNull RandomSource random
        ) {
            return new SmearEmitterParticle(level, x, y, z, options);
        }
    }
}
