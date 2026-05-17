package net.superkat.booyah.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.compat.StreetArtCompat;
import net.superkat.booyah.particles.splatana.DropletParticleOptions;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class SplatanaSwipe extends Projectile {
    public static final EntityDataAccessor<Integer> MAX_AGE_ID = SynchedEntityData.defineId(SplatanaSwipe.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> COLOR_ID = SynchedEntityData.defineId(SplatanaSwipe.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ALT_COLOR_A_ID = SynchedEntityData.defineId(SplatanaSwipe.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ALT_COLOR_B_ID = SynchedEntityData.defineId(SplatanaSwipe.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ALT_COLOR_C_ID = SynchedEntityData.defineId(SplatanaSwipe.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ROT_Z = SynchedEntityData.defineId(SplatanaSwipe.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER_ENTITY = SynchedEntityData.defineId(
            SplatanaSwipe.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE
    );
    public ExtraAnimInfo extraAnimA = new ExtraAnimInfo(0);
    public ExtraAnimInfo extraAnimB = new ExtraAnimInfo(1);
    public ExtraAnimInfo extraAnimC = new ExtraAnimInfo(2);

    public byte streetArtColorComponentId = 0;
    public boolean hasStreetArtPaint = false;
    public SplatanaSwipe(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public SplatanaSwipe(Level level, double x, double y, double z) {
        super(BooyahEntities.SPLATANA_SWIPE, level);
        this.setPos(x, y, z);
    }

    public SplatanaSwipe(ServerLevel serverLevel, LivingEntity livingEntity, ItemStack itemStack) {
        this(serverLevel, livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(MAX_AGE_ID, 16);
        entityData.define(COLOR_ID, -1);
        entityData.define(ALT_COLOR_A_ID, -1);
        entityData.define(ALT_COLOR_B_ID, -1);
        entityData.define(ALT_COLOR_C_ID, -1);
        entityData.define(ROT_Z, 0);
        entityData.define(OWNER_ENTITY, Optional.empty());
    }

    @Override
    public void tick() {
        HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        Vec3 position;
        this.updateRotation();

        if (result.getType() != HitResult.Type.MISS && this.isAlive()) {
            this.onHit(result);
            position = result.getLocation();
        } else {
            position = this.position().add(this.getDeltaMovement());
        }
        this.setPos(position);

        this.extraAnimA.update(this.tickCount);
        this.extraAnimB.update(this.tickCount);
        this.extraAnimC.update(this.tickCount);
        if (this.tickCount % 2 == 0 && this.level().isClientSide()) {
            int colorIndex = this.getRandom().nextInt(3);
            int color = switch (colorIndex) {
                case 1 -> this.getEntityData().get(ALT_COLOR_A_ID);
                case 2 -> this.getEntityData().get(ALT_COLOR_B_ID);
                case 3 -> this.getEntityData().get(ALT_COLOR_C_ID);
                default -> this.getEntityData().get(COLOR_ID);
            };
            Vec3 delta = this.getDeltaMovement().scale(0.5f);
            this.level().addParticle(new DropletParticleOptions(color), this.getX(), this.getY(), this.getZ(), delta.x, delta.y, delta.z);
        } else if (this.hasStreetArtPaint && this.tickCount % 2 == 0 && this.level() instanceof ServerLevel serverLevel) {
            SplatanaDroplet droplet = BooyahEntities.SPLATANA_DROPLET.create(this.level(), EntitySpawnReason.TRIGGERED);
            if (droplet != null) {
                droplet.setPos(this.getX(), this.getY() - 0.5f, this.getZ());
                droplet.setDeltaMovement(this.getDeltaMovement().scale(0.5f));
                droplet.setStreetArtColorComponentId(this.streetArtColorComponentId);
                droplet.setRange(this.getEntityData().get(ROT_Z) == 90 ? 1.5 : 1.25);
                droplet.setOwner(this.getOwner());
                serverLevel.addFreshEntity(droplet);
            }
        }

        super.tick();
        if (this.tickCount >= this.getMaxAge()) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity instanceof LivingEntity livingEntity && this.getOwnerReference() != null && this.getOwnerReference().matches(livingEntity)) return false;
        return super.canHitEntity(entity);
    }

    @Override
    protected void onHitEntity(final EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity entity = hitResult.getEntity();
        int damage = this.getEntityData().get(ROT_Z) == 90 ? 18 : 8;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        if (this.hasStreetArtPaint && this.level() instanceof ServerLevel serverLevel) {
            Vec3 splashOrigin = hitResult.getLocation()
                    .add(hitResult.getDirection().getUnitVec3().scale(0.3f))
                    .subtract(this.getDeltaMovement().scale(0.3f));

            Player owner = this.getOwner() instanceof Player player ? player : null;

            StreetArtCompat.createSplatanaSwipeSplash(owner, serverLevel, splashOrigin, this.getEntityData().get(ROT_Z) == 90 ? 3 : 2.5, this.streetArtColorComponentId);
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
    protected AABB makeBoundingBox(Vec3 position) {
        float width = 1.75f;
        float height = 0.75f;
        if (this.getEntityData().get(ROT_Z) == 90f) {
            width = 0.7f;
            height = 1.85f;
        }
        return AABB.ofSize(position, width, height, width);
    }

    public int getMaxAge() {
        return this.getEntityData().get(MAX_AGE_ID);
    }

    public void setMaxAge(int maxAge) {
        this.getEntityData().set(MAX_AGE_ID, maxAge);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.getEntityData().set(OWNER_ENTITY, Optional.ofNullable(owner).map(EntityReference::of));
    }

    @Nullable
    public EntityReference<LivingEntity> getOwnerReference() {
        return this.entityData.get(OWNER_ENTITY).orElse(null);
    }

    @Override
    public @Nullable Entity getOwner() {
        if (this.getOwnerReference() != null) {
            return this.level().getEntity(this.getOwnerReference().getUUID());
        }
        return null;
    }

    public void setStreetArtColorComponentId(byte streetArtColorComponentId) {
        this.streetArtColorComponentId = streetArtColorComponentId;
        this.hasStreetArtPaint = true;
    }

    public static final class ExtraAnimInfo {
        private final int index;
        private float x = 0;
        private float y = 0;
        private float prevX = 0;
        private float prevY = 0;

        public ExtraAnimInfo(int index) {
            this.index = index;
        }

        public float getX(float partialTicks) {
            return Mth.lerp(partialTicks, this.prevX, this.x);
        }

        public float getY(float partialTicks) {
            return Mth.lerp(partialTicks, this.prevY, this.y);
        }

        public void update(int age) {
            this.updateX(age);
            this.updateY(age);
        }

        public void updateX(int age) {
            this.prevX = this.x;
            this.x = getModelX(age, this.index);
        }

        public void updateY(int age) {
            this.prevY = this.y;
            this.y = getModelY(age, this.index);
        }

        // Mathematically accurate Trizooka shots with configurable distance (this makes me so happy)
        // https://www.desmos.com/calculator/f60n1p4fez
        private static float getModelX(int age, int extraIndex) {
            float size = (1f / 10f) * Math.min(age, 10);
            float distance = 0.15f * size;
            return (float) (Math.cos((-12 * (age - (4 * extraIndex))) + (120 * extraIndex) + 300) * distance);
        }

        private static float getModelY(int age, int extraIndex) {
            float size = (1f / 10f) * Math.min(age, 10);
            float distance = 0.045f * size;
            return (float) (Math.sin((-12 * (age - (4 * extraIndex))) + (120 * extraIndex) + 300) * distance);
        }
    }
}
