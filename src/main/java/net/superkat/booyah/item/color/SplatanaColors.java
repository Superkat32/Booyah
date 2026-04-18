package net.superkat.booyah.item.color;

import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.color.HSVColor;
import net.superkat.booyah.item.BooyahItems;

public class SplatanaColors {

    public static final SplatanaColorSet WHITE = new SplatanaColorSet(
            new HSVColor(192, 2, 94),
            Vec3.ZERO
    );

    public static final SplatanaColorSet PURPLE = new SplatanaColorSet(
            new HSVColor(280, 71, 100),
            new Vec3i(3, 25, 0)
    );

    public static SplatanaColorSet getSplatanaColorSet(ItemStack item) {
        if(item.has(BooyahItems.SPLATANA_COMPONENT)) {
            return item.get(BooyahItems.SPLATANA_COMPONENT).colorSet();
        }

        if (item.is(BooyahItems.STAMPER_WHITE)) return WHITE;
        if (item.is(BooyahItems.STAMPER_PURPLE)) return PURPLE;

        return WHITE;
    }

}
