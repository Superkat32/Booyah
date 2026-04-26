package net.superkat.booyah.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.superkat.booyah.balloon.BalloonChain;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class Balloon extends LivingEntity {

    public final AnimationState idleAnimationState = new AnimationState();
    @Nullable
    public BalloonChain chain = null; // Server only

    public Balloon(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        this.idleAnimationState.startIfStopped(this.tickCount);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        this.level().playSound(null, this.blockPosition(), SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.NEUTRAL, 1f, 1f);
        this.remove(RemovalReason.KILLED);
        return false;
    }

    public void playPopVisualsAndAudio() {

    }

    @Override
    public void onRemoval(RemovalReason reason) {
        if (this.chain != null && !this.level().isClientSide()) {
            this.chain.onBalloonPop(this);
        }
        super.onRemoval(reason);
    }

    public void setChain(BalloonChain chain) {
        this.chain = chain;
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
