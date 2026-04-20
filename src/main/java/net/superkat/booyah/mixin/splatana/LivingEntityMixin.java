package net.superkat.booyah.mixin.splatana;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.item.SplatanaManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements SplatanaPlayer {
    @Shadow
    protected abstract int getCurrentSwingDuration();

    @Unique
    public boolean booyah$isSplatanaSwinging = false;
    @Unique
    public int booyah$splatanaSwingTime = 0;
    @Unique
    public float booyah$splatanaAttackAnim = 0f;
    @Unique
    public float booyah$prevSplatanaAttackAnim = 0f;
    @Unique
    public boolean booyah$reverseSplatanaSwing = false;
    @Unique
    public boolean booyah$queuedReverseSplatanaSwing = false;
    @Unique
    public int booyah$splatanaSlashTime = 0;
    @Unique
    public int booyah$maxSplatanaSlashTime = 0;
    @Unique
    public float booyah$splatanaSlashAnim = 0f;
    @Unique
    public float booyah$prevSplatanaSlashAnim = 0f;
    @Unique
    public int booyah$dashTime = 0;
    @Unique
    public float booyah$dashAnim = 0f;
    @Unique
    public float booyah$prevDashAnim =0f;

    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("TAIL"))
    public void booyah$updateSplatanaSwinging(InteractionHand hand, boolean sendToSwingingEntity, CallbackInfo ci) {
        SplatanaManager.onSplatanaPlayerSwing((LivingEntity) (Object) this);
    }

    @Inject(method = "updateSwingTime", at = @At("TAIL"))
    public void booyah$updateSplatanaSwingTime(CallbackInfo ci) {
        // Called on server and client - Can't be bothered to access widener lol
        SplatanaManager.updateSplatanaPlayer((LivingEntity) (Object) this, this.getCurrentSwingDuration());
    }

    @Override
    public boolean booyah$isSplatanaSwinging() {
        return this.booyah$isSplatanaSwinging;
    }

    @Override
    public void booyah$setIsSplatanaSwinging(boolean swinging) {
        this.booyah$isSplatanaSwinging = swinging;
    }

    @Override
    public int booyah$getSplatanaSwingTime() {
        return this.booyah$splatanaSwingTime;
    }

    @Override
    public void booyah$setSplatanaSwingTime(int swingTime) {
        this.booyah$splatanaSwingTime = swingTime;
    }

    @Override
    public float booyah$splatanaAttackAnim() {
        return this.booyah$splatanaAttackAnim;
    }

    @Override
    public float booyah$prevSplatanaAttackAnim() {
        return this.booyah$prevSplatanaAttackAnim;
    }

    @Override
    public void booyah$setSplatanaAttackAnim(float anim) {
        this.booyah$prevSplatanaAttackAnim = this.booyah$splatanaAttackAnim;
        this.booyah$splatanaAttackAnim = anim;
    }

    @Override
    public boolean booyah$reverseSplatanaSwing() {
        return this.booyah$reverseSplatanaSwing;
    }

    @Override
    public void booyah$setReverseSplatanaSwing(boolean reversed) {
        this.booyah$reverseSplatanaSwing = reversed;
    }

    @Override
    public boolean booyah$queuedReverseUpdate() {
        return this.booyah$queuedReverseSplatanaSwing;
    }

    @Override
    public void booyah$setQueuedReverseUpdate(boolean reversed) {
        this.booyah$queuedReverseSplatanaSwing = reversed;
    }

    @Override
    public int booyah$splatanaSlashTime() {
        return this.booyah$splatanaSlashTime;
    }

    @Override
    public void booyah$setSplatanaSlashTime(int slashTime) {
        this.booyah$splatanaSlashTime = slashTime;
    }

    @Override
    public int booyah$maxSplatanaSlashTime() {
        return this.booyah$maxSplatanaSlashTime;
    }

    @Override
    public void booyah$setMaxSplatanaSlashTime(int maxSlashTime) {
        this.booyah$maxSplatanaSlashTime = maxSlashTime;
    }

    @Override
    public float booyah$splatanaSlashAnim() {
        return this.booyah$splatanaSlashAnim;
    }

    @Override
    public float booyah$prevSplatanaSlashAnim() {
        return this.booyah$prevSplatanaSlashAnim;
    }

    @Override
    public void booyah$setSplatanaSlashAnim(float anim) {
        this.booyah$prevSplatanaSlashAnim = this.booyah$splatanaSlashAnim;
        this.booyah$splatanaSlashAnim = anim;
    }

    @Override
    public int booyah$dashTime() {
        return this.booyah$dashTime;
    }

    @Override
    public void booyah$setDashTime(int dashTime) {
        this.booyah$dashTime = dashTime;
    }

    @Override
    public float booyah$dashAnim() {
        return this.booyah$dashAnim;
    }

    @Override
    public float booyah$prevDashAnim() {
        return this.booyah$prevDashAnim;
    }

    @Override
    public void booyah$setDashAnim(float anim) {
        this.booyah$prevDashAnim = this.booyah$dashAnim;
        this.booyah$dashAnim = anim;
    }
}
