package net.superkat.booyah.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.superkat.booyah.particles.balloon.BalloonBurstParticle;
import net.superkat.booyah.particles.balloon.BalloonPopParticle;
import net.superkat.booyah.particles.smear.SmearEmitterParticle;
import net.superkat.booyah.particles.smear.SmearParticle;

public class BooyahParticlesClient {

    public static void init() {
        ParticleProviderRegistry.getInstance().register(BooyahParticles.SMEAR_EMITTER, new SmearEmitterParticle.Provider());
        ParticleProviderRegistry.getInstance().register(BooyahParticles.SMEAR, SmearParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(BooyahParticles.BALLOON_POP, BalloonPopParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(BooyahParticles.BALLOON_BURST, BalloonBurstParticle.Provider::new);
    }
}
