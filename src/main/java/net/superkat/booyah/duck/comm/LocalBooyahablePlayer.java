package net.superkat.booyah.duck.comm;

import net.superkat.booyah.comm.BooyahClientManager;

public interface LocalBooyahablePlayer {

    int booyah$sneakTicks();
    void booyah$setSneakTicks(int ticks);
    default void booyah$subtractSneakTicks() {
        int ticks = this.booyah$sneakTicks();
        if (ticks > 0) {
            this.booyah$setSneakTicks(ticks - 1);
            if (ticks - 1 <= 0) {
                this.booyah$resetSneaksUntilBooyah();
            }
        }
    }
    default void booyah$resetSneakTicks() {
        this.booyah$setSneakTicks(BooyahClientManager.TICKS_BETWEEN_SNEAKS);
    }

    int booyah$getSneaksUntilBooyah();
    void booyah$setSneaksUntilBooyah(int sneaks);
    default void booyah$resetSneaksUntilBooyah() {
        this.booyah$resetSneaksUntilBooyah(false);
    }
    default void booyah$resetSneaksUntilBooyah(boolean further) {
        this.booyah$setSneaksUntilBooyah(further ? BooyahClientManager.FURTHER_SNEAKS_UNTIL_BOOYAH : BooyahClientManager.SNEAKS_UNTIL_BOOYAH);
    }

    boolean booyah$getWasSneaking();
    void booyah$setWasSneaking(boolean sneaking);

}
