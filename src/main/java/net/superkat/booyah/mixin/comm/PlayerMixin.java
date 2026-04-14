package net.superkat.booyah.mixin.comm;

import net.minecraft.world.entity.player.Player;
import net.superkat.booyah.comm.BooyahManager;
import net.superkat.booyah.duck.comm.BooyahablePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements BooyahablePlayer {
    @Unique
    public int booyah$tickCountOfBooyah = 0;
    @Unique
    public int booyah$booyahTicks = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    public void booyah$tickBooyahablePlayer(CallbackInfo ci) {
        BooyahManager.tickBooyahablePlayer((Player)(Object)this);
    }

    @Override
    public int booyah$tickCountOfBooyah() {
        return this.booyah$tickCountOfBooyah;
    }

    @Override
    public void booyah$setTickCountOfBooyah(int tickCount) {
        this.booyah$tickCountOfBooyah = tickCount;
    }

    @Override
    public int booyah$booyahTicks() {
        return this.booyah$booyahTicks;
    }

    @Override
    public void booyah$setBooyahTicks(int ticks) {
        this.booyah$booyahTicks = ticks;
    }
}
