package net.superkat.booyah.mixin.splatana;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.item.SplatanaAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements SplatanaPlayer {
    @Shadow
    public boolean swinging;

    @Shadow
    protected abstract int getCurrentSwingDuration();

    @Shadow
    private float speed;
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
    public boolean booyah$isFirstSwing = true;

    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("TAIL"))
    public void booyah$updateSplatanaSwinging(InteractionHand hand, boolean sendToSwingingEntity, CallbackInfo ci) {
        if (this.swinging) { // Only activate if original arm is swinging
            this.booyah$isSplatanaSwinging = true;
            this.booyah$splatanaSwingTime = -1; // Start at -1 for buffer?
        }
    }

    @Inject(method = "updateSwingTime", at = @At("TAIL"))
    public void booyah$updateSplatanaSwingTime(CallbackInfo ci) {
        // Can't be bothered to access widener lol
        SplatanaAnimations.updateSplatanaSwingTime((LivingEntity) (Object) this, this.getCurrentSwingDuration());
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
    public boolean booyah$firstSwing() {
        return this.booyah$isFirstSwing;
    }

    @Override
    public void booyah$setFirstSwing(boolean isFirstSwing) {
        this.booyah$isFirstSwing = isFirstSwing;
    }
}
