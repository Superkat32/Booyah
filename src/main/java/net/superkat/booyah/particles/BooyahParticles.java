package net.superkat.booyah.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.particles.smear.SmearEmitterParticleOptions;
import net.superkat.booyah.particles.smear.SplatanaSwingParticleOptions;

public class BooyahParticles {

    public static final ParticleType<SmearEmitterParticleOptions> SMEAR_EMITTER = FabricParticleTypes.complex(true, SmearEmitterParticleOptions.CODEC, SmearEmitterParticleOptions.STREAM_CODEC);
    public static final ParticleType<SplatanaSwingParticleOptions> SPLATANA_SWING = FabricParticleTypes.complex(true, SplatanaSwingParticleOptions.CODEC, SplatanaSwingParticleOptions.STREAM_CODEC);

    public static void init() {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Booyah.id("smear_emitter"), SMEAR_EMITTER);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Booyah.id("splatana_swing"), SPLATANA_SWING);
    }

}
