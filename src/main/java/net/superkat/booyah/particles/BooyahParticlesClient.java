package net.superkat.booyah.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.superkat.booyah.particles.smear.SmearEmitterParticle;
import net.superkat.booyah.particles.smear.SplatanaSwingParticle;

public class BooyahParticlesClient {

    public static void init() {
        ParticleProviderRegistry.getInstance().register(BooyahParticles.SMEAR_EMITTER, new SmearEmitterParticle.Provider());
        ParticleProviderRegistry.getInstance().register(BooyahParticles.SPLATANA_SWING, SplatanaSwingParticle.Provider::new);
    }
}
