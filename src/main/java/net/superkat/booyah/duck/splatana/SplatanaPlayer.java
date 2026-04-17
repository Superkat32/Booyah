package net.superkat.booyah.duck.splatana;

// This is used on the server AND client
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

    // THIS WAS SO ANNOYING AND FRUSTRATING IT'S NOT EVEN FUNNY
    // On attack, Mojang sets the player's "attackTime" to "-1", meaning it can start at 0 for rendering
    // But 0 is also used for when no attacks are happening at all, and because of the timings of everything,
    // there'd be a SINGLE FRAME between alternating swipes where the reverse happened too early
    // This took like 40 minutes to get a half decent solution while accounting for networking and all
    boolean booyah$queuedReverseUpdate();
    void booyah$setQueuedReverseUpdate(boolean reversed);

}
