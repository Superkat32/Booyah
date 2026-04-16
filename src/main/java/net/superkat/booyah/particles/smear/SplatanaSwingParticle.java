package net.superkat.booyah.particles.smear;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;
import org.jspecify.annotations.NonNull;

@Environment(EnvType.CLIENT)
public class SplatanaSwingParticle extends SingleQuadParticle {
    private final SpriteSet sprites;
    public boolean reversed;
    public SplatanaSwingParticle(ClientLevel level, double x, double y, double z, SplatanaSwingParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;
        this.lifetime = 4;
        this.reversed = options.reversed();
        this.rCol = ARGB.redFloat(options.color());
        this.gCol = ARGB.greenFloat(options.color());
        this.bCol = ARGB.blueFloat(options.color());
        this.quadSize = 1.5f;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
        Quaternionf rotation = new Quaternionf();
//        this.getFacingCameraMode().setRotation(rotation, camera, partialTickTime);
        rotation.set(camera.rotation().x, 0, camera.rotation().z, 0);
        rotation.rotateX((float) Math.toRadians(90));
        rotation.rotateZ((float) Math.toRadians(180));
        this.extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
        rotation.rotateXYZ((float) Math.toRadians(180), 0, 0);
        this.extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public int getLightCoords(final float a) {
        return 15728880;
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    protected float getU0() {
        return this.reversed ? this.sprite.getU1() : this.sprite.getU0();
    }

    @Override
    protected float getU1() {
        return this.reversed ? this.sprite.getU0() : this.sprite.getU1();
    }

    @Override
    protected float getV0() {
//        return this.reversed ? this.sprite.getV1() : this.sprite.getV0();
        return super.getV0();
    }

    @Override
    protected float getV1() {
//        return this.reversed ? this.sprite.getV0() : this.sprite.getV1();
        return super.getV1();
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SplatanaSwingParticleOptions> {
        private final SpriteSet sprites;

        public Provider(final SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(
                final SplatanaSwingParticleOptions options,
                final @NonNull ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final @NonNull RandomSource random
        ) {
            return new SplatanaSwingParticle(level, x, y, z, options, this.sprites);
        }
    }
}
