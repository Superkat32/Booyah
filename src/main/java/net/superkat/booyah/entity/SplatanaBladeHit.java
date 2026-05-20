package net.superkat.booyah.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SplatanaBladeHit extends Entity implements TraceableEntity {
    public static final EntityDataAccessor<Boolean> VERTICAL = SynchedEntityData.defineId(SplatanaBladeHit.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    public EntityReference<LivingEntity> owner;
    public ItemStack attackingStack = ItemStack.EMPTY;
    public int lifetime = 8;
    public Set<UUID> hitEntities = new HashSet<>();

    public SplatanaBladeHit(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        if (this.level() instanceof ServerLevel serverLevel
                && this.getOwner() != null
                && this.getOwner() instanceof Player ownerPlayer
                && !ownerPlayer.isDeadOrDying()
        ) {
//            Entity owner = this.getOwner();
//            Player playerOwner = owner instanceof Player playOwner ? playOwner : null;
            for (Entity entity : serverLevel.getEntities(this, this.getBoundingBox())) {
                if (this.hitEntities.contains(entity.getUUID())) continue;
                if (entity.is(ownerPlayer)) continue;
                if (!(entity instanceof LivingEntity livingEntity)) continue;
                if (livingEntity instanceof Player player && !ownerPlayer.canHarmPlayer(player)) continue;

                DamageSource damageSource = attackingStack.getDamageSource(ownerPlayer, () -> ownerPlayer.damageSources().playerAttack(ownerPlayer));
                livingEntity.hurtServer(serverLevel, damageSource, this.getEntityData().get(VERTICAL) ? 12 : 6);
                this.hitEntities.add(entity.getUUID());
            }
        }

        if (this.getOwner() != null && !this.getOwner().isDeadOrDying()) {
            LivingEntity owner = this.getOwner();
            Vec3 point = new Vec3(0, 1, 1f);
            point = point.yRot(-owner.getYRot() * (float) (Math.PI / 180.0));

            this.xOld = this.getX();
            this.yOld = this.getY();
            this.zOld = this.getZ();
            this.setPos(owner.position().add(point));
            this.setDeltaMovement(owner.getDeltaMovement());
        }

        super.tick();

        if (this.tickCount >= this.lifetime) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    protected boolean canHitEntity(Entity entity) {
//        if (entity instanceof LivingEntity livingEntity && this.getOwner() != null) {
//            if (this.getOwner().is(livingEntity)) return false;
//            if (livingEntity instanceof Player player && this.getOwner() instanceof Player ownerPlayer && !ownerPlayer.canHarmPlayer(player)) return false;
//        }
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(VERTICAL, false);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected AABB makeBoundingBox(Vec3 position) {
        float width = 1.5f;
        float height = 0.75f;
        if (this.getEntityData().get(VERTICAL)) {
            width = 1f;
            height = 1.85f;
        }
        return AABB.ofSize(position, width, height, width);
    }

    @Nullable
    public LivingEntity getOwner() {
        return EntityReference.getLivingEntity(this.owner, this.level());
    }

    public void setOwner(@Nullable final LivingEntity owner) {
        this.owner = EntityReference.of(owner);
    }

    public void setAttackingStack(ItemStack attackingStack) {
        this.attackingStack = attackingStack;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }
}
