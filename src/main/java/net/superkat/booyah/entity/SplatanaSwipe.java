package net.superkat.booyah.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SplatanaSwipe extends Projectile {
    public static final EntityDataAccessor<Integer> COLOR_ID = SynchedEntityData.defineId(SplatanaSwipe.class, EntityDataSerializers.INT);
    public int maxAge = 100;
    public SplatanaSwipe(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public SplatanaSwipe(Level level, double x, double y, double z) {
        super(BooyahEntities.SPLATANA_SWIPE, level);
        this.setPos(x, y, z);
    }

    public SplatanaSwipe(ServerLevel serverLevel, LivingEntity livingEntity, ItemStack itemStack) {
        this(serverLevel, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(COLOR_ID, -1);
    }

    @Override
    public void tick() {
        this.setPos(this.position().add(this.getDeltaMovement()));
        this.updateRotation();
        super.tick();
        if (this.tickCount >= this.getMaxAge()) {
            this.remove(RemovalReason.KILLED);
        }
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
