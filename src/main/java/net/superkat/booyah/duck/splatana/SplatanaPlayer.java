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

    boolean booyah$reverseSplatanaSwing();
    void booyah$setReverseSplatanaSwing(boolean reversed);

    // THIS WAS SO ANNOYING AND FRUSTRATING IT'S NOT EVEN FUNNY
    // On attack, Mojang sets the player's "attackTime" to "-1", meaning it can start at 0 for rendering
    // But 0 is also used for when no attacks are happening at all, and because of the timings of everything,
    // there'd be a SINGLE FRAME between alternating swipes where the reverse happened too early
    // This took like 40 minutes to get a half decent solution while accounting for networking and all
    boolean booyah$queuedReverseUpdate();
    void booyah$setQueuedReverseUpdate(boolean reversed);

    int booyah$splatanaSlashTime();
    void booyah$setSplatanaSlashTime(int slashTime);

    int booyah$maxSplatanaSlashTime();
    void booyah$setMaxSplatanaSlashTime(int maxSlashTime);

    float booyah$splatanaSlashAnim();
    float booyah$prevSplatanaSlashAnim();
    void booyah$setSplatanaSlashAnim(float anim);
    default float booyah$getSplatanaSlashAnim(float partialTicks) {
        float diff = this.booyah$splatanaSlashAnim() - this.booyah$prevSplatanaSlashAnim();
        if (diff < 0) diff++;

        return this.booyah$prevSplatanaSlashAnim() + diff * partialTicks;
    }

    int booyah$dashTime();
    void booyah$setDashTime(int dashTime);

    float booyah$dashAnim();
    float booyah$prevDashAnim();
    void booyah$setDashAnim(float anim);
    default float booyah$getDashAnim(float partialTicks) {
        float diff = this.booyah$dashAnim() - this.booyah$prevDashAnim();
        if (diff < 0) diff++;

        return this.booyah$prevDashAnim() + diff * partialTicks;
    }

}
