package net.superkat.booyah.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Ease;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.balloon.BalloonChain;
import net.superkat.booyah.particles.BooyahParticles;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class Balloon extends LivingEntity {

    public final AnimationState idleAnimationState = new AnimationState();
    @Nullable
    public BalloonChain chain = null; // Server only
    @Nullable
    public BlockPos chainPos = null; // Also server only
    public boolean floatingAway = false;
    public int floatAwayTicks = 0;
    public int ticksUntilFloatAway = -1;

    public Balloon(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }



    @Override
    public void tick() {
        super.tick();
        this.idleAnimationState.startIfStopped(this.tickCount);

        if (this.ticksUntilFloatAway > 0) {
            this.ticksUntilFloatAway--;
            if (this.ticksUntilFloatAway <= 0) {
                this.floatAway();
            }
        }

        if(this.floatingAway) {
            this.floatAwayTicks++;
            if (this.floatAwayTicks % 40 == 0) {
                this.playSound(SoundEvents.BREEZE_IDLE_GROUND, 1f, 1f);
            }
            if (this.floatAwayTicks <= 100) {
                float deltaY = Ease.inOutCirc(Math.min(1f, this.floatAwayTicks / 100f)) * 0.1f;
                this.setDeltaMovement(this.getDeltaMovement().x, deltaY, this.getDeltaMovement().z);
            } else {
                this.addDeltaMovement(new Vec3(0, 0.01f, 0));
            }
            if (this.floatAwayTicks >= 180) {
                this.remove(RemovalReason.DISCARDED);
            }
        }

        // Check distance from initial chain every 2 seconds to reduce lag
        if (this.tickCount % 40 == 0 && !this.level().isClientSide() && this.chainPos != null) {
            if (this.blockPosition().getCenter().distanceToSqr(this.chainPos.getCenter()) >= 256) {
                this.floatAway();
            }
        }
    }

    public void floatAway() {
        this.floatingAway = true;
        this.playSound(SoundEvents.BREEZE_IDLE_GROUND, 1f, 1f);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        this.remove(RemovalReason.KILLED);
        return false;
    }

    public void playPopVisualsAndAudio() {
//        this.level().playPlayerSound(SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.NEUTRAL, 1f, 1f);
//        this.level().playPlayerSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.NEUTRAL, 0.5f, 1.5f);
        this.level().playLocalSound(this.blockPosition(), SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.NEUTRAL, 1f, 1f, true);
        this.level().playLocalSound(this.blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.NEUTRAL, 0.5f, 1.5f, true);

        for (int i = 0; i < 24; i++) {
            float velX = floatRange(-0.2f, 0.2f);
            float velY = floatRange(-0.05f, 0.45f);
            float velZ = floatRange(-0.2f, 0.2f);
            this.level().addParticle(BooyahParticles.BALLOON_POP, this.getX(), this.getY(), this.getZ(), velX, velY, velZ);
        }
        this.level().addParticle(BooyahParticles.BALLOON_BURST, this.getX(), this.getY() + 0.35f, this.getZ(), 0, 0, 0);
    }

    private float floatRange(float min, float max) {
        return this.random.nextFloat() * (max - min) + min;
    }

    @Override
    public void onRemoval(@NonNull RemovalReason reason) {
        if (this.level().isClientSide()) {
            this.playPopVisualsAndAudio();
        } else {
            if (reason == RemovalReason.KILLED && this.chain != null) {
                this.chain.onBalloonPop(this);
            } else if (this.chain != null) {
                this.chain.onBalloonDespawn(this);
            }
        }
        super.onRemoval(reason);
    }

    public void setChain(BalloonChain chain) {
        this.chain = chain;
    }

    public void setChainPos(BlockPos pos) {
        this.chainPos = pos;
    }

    public BlockPos getChainPos() {
        return this.chainPos;
    }

    public void setTicksUntilFloatAway(int ticks) {
        this.ticksUntilFloatAway = ticks;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WIND_CHARGE_BURST.value();
    }

    @Override
    public @NonNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createLivingAttributes().add(Attributes.STEP_HEIGHT, 0.0);
    }
}
