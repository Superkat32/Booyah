package net.superkat.booyah.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.entity.SplatanaSwipe;
import net.superkat.booyah.item.color.SplatanaColorSet;
import net.superkat.booyah.item.color.SplatanaColors;
import net.superkat.booyah.particles.smear.SmearEmitterParticleOptions;

public class SplatanaItem extends Item {
    public static final float PLAYER_SPEED_SWING_REDUCER_AMOUNT = 0.6f;

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -1.785f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    public SplatanaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!(player instanceof SplatanaPlayer splatanaPlayer)) return InteractionResult.FAIL;
        if (splatanaPlayer.booyah$isSplatanaSwinging() || (splatanaPlayer.booyah$maxSplatanaSlashTime() > 0 && splatanaPlayer.booyah$splatanaSlashTime() <= 10)) {
            return InteractionResult.FAIL;
        }
        splatanaPlayer.booyah$setSplatanaSlashTime(0);
        splatanaPlayer.booyah$setMaxSplatanaSlashTime(-1);
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        if (!(entity instanceof Player player)) return false;
        if (!(player instanceof SplatanaPlayer splatanaPlayer)) return false;
        int timeHeld = this.getUseDuration(itemStack, entity) - remainingTime;
        if (timeHeld < 8) return false;

        splatanaPlayer.booyah$setSplatanaSlashTime(0);
        splatanaPlayer.booyah$setMaxSplatanaSlashTime(28);

        float speed = itemStack.has(BooyahItems.SPLATANA_COMPONENT) ? itemStack.get(BooyahItems.SPLATANA_COMPONENT).dashAmount() : 1f;
        Vec3 look = entity.getLookAngle();
        Vec3 direction = look.addLocalCoordinates(new Vec3(0, 0, 1)).scale(0.55f);
        entity.addDeltaMovement(direction);

        float distanceFromPlayer = 3f;
        double dx = -Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)) * distanceFromPlayer;
        double dz = Mth.cos(player.getYRot() * (float) (Math.PI / 180.0)) * distanceFromPlayer;

        SplatanaColorSet colorSet = SplatanaColors.getSplatanaColorSet(player.getMainHandItem());
        level.addParticle(new SmearEmitterParticleOptions(2, 2, 16, colorSet, false, -90, player.getYRot()), player.getX() + dx, player.getY(0.5), player.getZ() + dz, dx, 0, dz);
        if (level instanceof ServerLevel serverLevel) {
            SplatanaSwipe swipe = Projectile.spawnProjectileFromRotation(SplatanaSwipe::new, serverLevel, itemStack, player, 1.0F, 1.5F, 0);
            int color = itemStack.get(BooyahItems.SPLATANA_COMPONENT).colorSet().getRandomColor(entity.getRandom(), 0);
            swipe.getEntityData().set(SplatanaSwipe.COLOR_ID, color);
            swipe.getEntityData().set(SplatanaSwipe.ROT_Z, 90);
//            swipe.setDeltaMovement(2, 0, 0);
//            swipe.addDeltaMovement(new Vec3(3, 0, 0));
        }
        return true;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 72000;
    }

//    @Override
//    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
//        return new SplatanaSwipe(level, position.x(), position.y(), position.z());
//    }
}
