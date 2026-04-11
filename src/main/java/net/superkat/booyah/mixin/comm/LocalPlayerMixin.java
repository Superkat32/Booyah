package net.superkat.booyah.mixin.comm;

import net.minecraft.client.player.LocalPlayer;
import net.superkat.booyah.comm.BooyahClientManager;
import net.superkat.booyah.duck.LocalBooyahablePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin implements LocalBooyahablePlayer {
    @Unique
    public int booyah$ticksSinceSneak = 0;
    @Unique
    public int booyah$sneaksUntilBooyah = BooyahClientManager.SNEAKS_UNTIL_BOOYAH;
    @Unique
    public boolean booyah$wasSneaking = false;

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void booyah$aiStepBooyahableLocalPlayer(CallbackInfo ci) {
        BooyahClientManager.aiStepLocalPlayer((LocalPlayer)(Object)this);
    }

    @Override
    public int booyah$sneakTicks() {
        return this.booyah$ticksSinceSneak;
    }

    @Override
    public void booyah$setSneakTicks(int ticks) {
        this.booyah$ticksSinceSneak = ticks;
    }

    @Override
    public int booyah$getSneaksUntilBooyah() {
        return this.booyah$sneaksUntilBooyah;
    }

    @Override
    public void booyah$setSneaksUntilBooyah(int sneaks) {
        this.booyah$sneaksUntilBooyah = sneaks;
    }

    @Override
    public boolean booyah$getWasSneaking() {
        return this.booyah$wasSneaking;
    }

    @Override
    public void booyah$setWasSneaking(boolean sneaking) {
        this.booyah$wasSneaking = sneaking;
    }
}
