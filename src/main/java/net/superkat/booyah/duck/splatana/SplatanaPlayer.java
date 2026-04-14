package net.superkat.booyah.duck.splatana;

public interface SplatanaPlayer {

    boolean booyah$isSplatanaSwinging();
    void booyah$setIsSplatanaSwinging(boolean swinging);

    int booyah$getSplatanaSwingTime();
    void booyah$setSplatanaSwingTime(int swingTime);

    float booyah$splatanaAttackAnim();
    float booyah$prevSplatanaAttackAnim();
    void booyah$setSplatanaAttackAnim(float anim);
    default float booyah$getSplatanaAttackAnim(float partialTicks) {
        float diff = this.booyah$splatanaAttackAnim() - this.booyah$prevSplatanaAttackAnim();
        if (diff < 0) diff++;

        return this.booyah$prevSplatanaAttackAnim() + diff * partialTicks;
    }

    boolean booyah$firstSwing();
    void booyah$setFirstSwing(boolean isFirstSwing);

    boolean booyah$reverseSplatanaSwing();
    void booyah$setReverseSplatanaSwing(boolean reversed);

}
