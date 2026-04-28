package net.superkat.booyah.particles.zoom;

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
import org.joml.Quaternionf;
import org.jspecify.annotations.NonNull;

public class ZoomParticle extends SingleQuadParticle {
    private static final float I_CONTINUE_TO_TURN_DEGREES_INTO_U_TURN_RADIANS = (float) Math.toRadians(180f);

    public float yaw;
    public float pitch;
    public float stretch;
    public ZoomParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, ZoomParticleOptions options, SpriteSet sprite) {
        super(level, x, y, z, xa, ya, za, sprite.first());

        this.yaw = options.yaw();
        this.pitch = options.pitch();
        this.stretch = options.stretch();
        this.xd = xa;
        this.yd = ya;
        this.zd = za;

        this.lifetime = 6;
        this.quadSize = 0.15f + this.random.nextFloat() / 2f;
        this.setColorFromInt(options.color());

        this.setSprite(sprite.get(this.random));
    }

    @Override
    public void tick() {
        int fadeOutTicks = 5;
        int nonFadeOutTicks = this.lifetime - fadeOutTicks;
        if (this.age >= nonFadeOutTicks) {
            this.alpha = 1f - (float) (this.age - nonFadeOutTicks) / fadeOutTicks;
        }
        if (this.alpha <= 0f) this.remove();

        super.tick();
    }

    @Override
    public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
        Quaternionf quaternionf = new Quaternionf();
        if (this.roll != 0) {
            quaternionf.rotateZ(Mth.lerp(partialTickTime, this.oRoll, this.roll));
        }

        quaternionf.rotateY((float) Math.toRadians(this.yaw));
        quaternionf.rotateZ((float) Math.toRadians(this.pitch));
        this.extractRotatedQuad(particleTypeRenderState, camera, quaternionf, partialTickTime);
        quaternionf.rotateY(I_CONTINUE_TO_TURN_DEGREES_INTO_U_TURN_RADIANS);
        quaternionf.rotateZ(I_CONTINUE_TO_TURN_DEGREES_INTO_U_TURN_RADIANS);
        this.extractRotatedQuad(particleTypeRenderState, camera, quaternionf, partialTickTime);
    }

    private void setColorFromInt(int color) {
        float red = ARGB.redFloat(color);
        float green = ARGB.greenFloat(color);
        float blue = ARGB.blueFloat(color);
        float alpha = ARGB.alphaFloat(color);
        this.setColor(red, green, blue);
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static record Provider(SpriteSet sprites) implements ParticleProvider<ZoomParticleOptions> {
        public Particle createParticle(
                final ZoomParticleOptions options,
                final @NonNull ClientLevel level,
                final double x,
                final double y,
                final double z,
                final double xAux,
                final double yAux,
                final double zAux,
                final @NonNull RandomSource random
        ) {
            return new ZoomParticle(level, x, y, z, xAux, yAux, zAux, options, this.sprites);
        }
    }
}
