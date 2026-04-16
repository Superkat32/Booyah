package net.superkat.booyah.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.particles.smear.SmearEmitterParticleOptions;

public class SplatanaItem extends Item {
    public static final float PLAYER_SPEED_SWING_REDUCER_AMOUNT = 0.6f;

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -1.785f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    public static void spawnSplatanaSwingParticles(LivingEntity player, ServerLevel serverLevel) {
        double dx = -Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)) * 0.8f;
        double dz = Mth.cos(player.getYRot() * (float) (Math.PI / 180.0)) * 0.8f;
        int color = ARGB.colorFromFloat(1f, 0.71f, 0.37f, 0.92f);
        boolean reversed = (player instanceof SplatanaPlayer splatanaPlayer && !splatanaPlayer.booyah$firstSwing() && !splatanaPlayer.booyah$reverseSplatanaSwing());
//        serverLevel.sendParticles(new SplatanaSwingParticleOptions(color, reversed), player.getX() + dx, player.getY(0.5), player.getZ() + dz, 0, dx, 0, dz, 0);
        serverLevel.sendParticles(new SmearEmitterParticleOptions(1, 1, 16, color, reversed), player.getX() + dx, player.getY(0.4), player.getZ() + dz, 0, dx, 0, dz, 0);
    }

    public SplatanaItem(Properties properties) {
        super(properties);
    }



    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }
}
