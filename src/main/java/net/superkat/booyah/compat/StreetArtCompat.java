package net.superkat.booyah.compat;

import com.streetart.AllDataComponents;
import com.streetart.PermissionUtil;
import com.streetart.SplashUtil;
import com.streetart.component.ColorComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.entity.SplatanaSwipe;

public class StreetArtCompat {
    public static byte setPaintColor(SplatanaSwipe swipe, ItemStack itemStack) {
        if (itemStack.has(AllDataComponents.COLOR)) {
            ColorComponent colorComponent = ColorComponent.getOrDefaultComponent(itemStack, ColorComponent.CLEAR);
            swipe.setStreetArtColorComponentId(colorComponent.id);
            return colorComponent.id;
        } return 0;
    }

    public static void createSplatanaSwipeSplash(Player owner, ServerLevel level, Vec3 splashOrigin, double range, byte colorId) {
        SplashUtil.createPaintSplash(owner, level, splashOrigin,
                range, 1000, 1f,
                SplashUtil.VariableThreshold.perlin(level.getRandom()),
                colorId,
                b -> PermissionUtil.splashingAllowed(b, level, owner)
        );
    }
}
