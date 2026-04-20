package net.superkat.booyah.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;

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
        if (player instanceof SplatanaPlayer splatanaPlayer && splatanaPlayer.booyah$isSplatanaSwinging()) {
            return InteractionResult.FAIL;
        }
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

        splatanaPlayer.booyah$setSplatanaSlashTime(28);
        splatanaPlayer.booyah$setMaxSplatanaSlashTime(28);

        float speed = itemStack.has(BooyahItems.SPLATANA_COMPONENT) ? itemStack.get(BooyahItems.SPLATANA_COMPONENT).dashAmount() : 1f;
        Vec3 look = entity.getLookAngle();
        Vec3 direction = look.addLocalCoordinates(new Vec3(0, 0, 1)).scale(0.55f);
        entity.addDeltaMovement(direction);
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
}
