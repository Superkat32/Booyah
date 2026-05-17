package net.superkat.booyah.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.compat.StreetArtCompat;

public class SplatanaDroplet extends ThrowableProjectile {

    public byte streetArtColorComponentId = 0;
    public boolean hasStreetArtPaint = false;
    public double range = 3;
    public SplatanaDroplet(EntityType<SplatanaDroplet> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {

    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        if (this.hasStreetArtPaint && this.level() instanceof ServerLevel serverLevel) {
            Vec3 splashOrigin = hitResult.getLocation()
                    .add(hitResult.getDirection().getUnitVec3().scale(0.3f))
                    .subtract(this.getDeltaMovement().scale(0.3f));

            Player owner = this.getOwner() instanceof Player player ? player : null;

            StreetArtCompat.createSplatanaSwipeSplash(owner, serverLevel, splashOrigin, this.range, this.streetArtColorComponentId);
        }
        super.onHitBlock(hitResult);
    }

    @Override
    protected void onHit(final HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_SPLASH, SoundSource.NEUTRAL, 0.3f, 1, false);
        }
        super.handleEntityEvent(id);
    }

    public void setStreetArtColorComponentId(byte streetArtColorComponentId) {
        this.streetArtColorComponentId = streetArtColorComponentId;
        this.hasStreetArtPaint = true;
    }

    public void setRange(double range) {
        this.range = range;
    }
}
