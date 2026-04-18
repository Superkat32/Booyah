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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.jspecify.annotations.NonNull;

@Environment(EnvType.CLIENT)
public class SmearParticle extends SingleQuadParticle {
    private final SpriteSet sprites;
    public boolean reversed;
    public float rotX;
    public float rotY;
    public SmearParticle(ClientLevel level, double x, double y, double z, SmearParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;
        this.lifetime = 4;
        this.reversed = options.reversed();
        this.rotX = options.rotX();
        this.rotY = options.rotY();
        this.rCol = ARGB.redFloat(options.color());
        this.gCol = ARGB.greenFloat(options.color());
        this.bCol = ARGB.blueFloat(options.color());
        this.quadSize = 1.6f;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
        Quaternionf rotation = new Quaternionf();
        rotation.rotateX((float) Math.toRadians(-90));
        rotation.rotateZ((float) Math.toRadians(-this.rotY + 180));
        rotation.rotateY((float) Math.toRadians(-this.rotX));
        this.extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime, this.reversed);
        rotation.rotateY((float) Math.toRadians(180));
        this.extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime, !this.reversed);
    }

    protected void extractRotatedQuad(
            final QuadParticleRenderState particleTypeRenderState, final Camera camera, final Quaternionf rotation, final float partialTickTime, boolean flipTexture
    ) {
        Vec3 pos = camera.position();
        float x = (float)(Mth.lerp(partialTickTime, this.xo, this.x) - pos.x());
        float y = (float)(Mth.lerp(partialTickTime, this.yo, this.y) - pos.y());
        float z = (float)(Mth.lerp(partialTickTime, this.zo, this.z) - pos.z());
        this.extractRotatedQuad(particleTypeRenderState, rotation, x, y, z, partialTickTime, flipTexture);
    }

    protected void extractRotatedQuad(
            final QuadParticleRenderState particleTypeRenderState, final Quaternionf rotation, final float x, final float y, final float z, final float partialTickTime, boolean flipTexture
    ) {
        particleTypeRenderState.add(
                this.getLayer(),
                x,
                y,
                z,
                rotation.x,
                rotation.y,
                rotation.z,
                rotation.w,
                this.getQuadSize(partialTickTime),
                flipTexture ? this.getU1() : this.getU0(),
                flipTexture ? this.getU0() : this.getU1(),
                this.getV0(),
                this.getV1(),
                ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol),
                this.getLightCoords(partialTickTime)
        );
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

//    @Override
//    protected float getU0() {
//        return this.reversed ? this.sprite.getU1() : this.sprite.getU0();
//    }
//
//    @Override
//    protected float getU1() {
//        return this.reversed ? this.sprite.getU0() : this.sprite.getU1();
//    }
//
//    @Override
//    protected float getV0() {
////        return this.reversed ? this.sprite.getV1() : this.sprite.getV0();
//        return super.getV0();
//    }
//
//    @Override
//    protected float getV1() {
////        return this.reversed ? this.sprite.getV0() : this.sprite.getV1();
//        return super.getV1();
//    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SmearParticleOptions> {
        private final SpriteSet sprites;

        public Provider(final SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(
                final SmearParticleOptions options,
                final @NonNull ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final @NonNull RandomSource random
        ) {
            return new SmearParticle(level, x, y, z, options, this.sprites);
        }
    }
}
