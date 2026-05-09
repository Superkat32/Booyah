package net.superkat.booyah.item.color;

import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.color.HSVColor;
import net.superkat.booyah.item.BooyahItems;

import java.util.List;

public class SplatanaColors {

    public static final SplatanaColorSet WHITE = new SplatanaColorSet(
            new HSVColor(220, 1, 95),
            new Vec3i(20, 8, 5)
    );

    public static final SplatanaColorSet LIGHT_GRAY = new SplatanaColorSet(
            new HSVColor(240, 1, 58),
            new Vec3i(4, 7, 10)
    );

    public static final SplatanaColorSet GRAY = new SplatanaColorSet(
            new HSVColor(0, 0, 35),
            new Vec3i(5, 5, 10)
    );

    public static final SplatanaColorSet BLACK = new SplatanaColorSet(
            new HSVColor(242, 36, 23),
            new Vec3i(5, 3, 10)
    );

    public static final SplatanaColorSet BROWN = new SplatanaColorSet(
            new HSVColor(18, 55, 52),
            new Vec3i(5, 3, 10)
    );

    public static final SplatanaColorSet RED = new SplatanaColorSet(
            new HSVColor(355, 72, 84),
            new Vec3i(5, 20, 0)
    );

    public static final SplatanaColorSet ORANGE = new SplatanaColorSet(
            new HSVColor(36, 85, 95),
            new Vec3i(4, 15, 0)
    );

    public static final SplatanaColorSet YELLOW = new SplatanaColorSet(
            new HSVColor(45, 78, 92),
            new Vec3i(6, 22, 2)
    );

    public static final SplatanaColorSet LIME = new SplatanaColorSet(
            new HSVColor(84, 75, 85),
            new Vec3i(4, 25, 2)
    );

    public static final SplatanaColorSet GREEN = new SplatanaColorSet(
            new HSVColor(78, 83, 40),
            new Vec3i(5, 10, 25)
    );

    public static final SplatanaColorSet CYAN = new SplatanaColorSet(
            new HSVColor(191, 54, 61),
            new Vec3i(6, 17, 26)
    );

    public static final SplatanaColorSet LIGHT_BLUE = new SplatanaColorSet(
            new HSVColor(209, 55, 100),
            new Vec3i(7, 15, 0)
    );

    public static final SplatanaColorSet BLUE = new SplatanaColorSet(
            new HSVColor(216, 80, 100),
            new Vec3i(8, 20, 24)
    );

    public static final SplatanaColorSet PURPLE = new SplatanaColorSet(
            new HSVColor(280, 71, 100),
            new Vec3i(3, 25, 0)
    );

    public static final SplatanaColorSet MAGENTA = new SplatanaColorSet(
            new HSVColor(303, 55, 80),
            new Vec3i(7, 15, 15)
    );

    public static final SplatanaColorSet PINK = new SplatanaColorSet(
            new HSVColor(330, 25, 95),
            new Vec3i(12, 25, 2)
    );

    public static final SplatanaColorSet TRANS = new SplatanaColorSet(
            List.of(
                    new HSVColor(327, 5, 100),
                    new HSVColor(322, 27, 97),
                    new HSVColor(193, 30, 100)
            ),
            new Vec3(3 / 360d, 25 / 100d, 0)
    );

    public static SplatanaColorSet getSplatanaColorSet(ItemStack item) {
        if(item.has(BooyahItems.SPLATANA_COMPONENT)) {
            return item.get(BooyahItems.SPLATANA_COMPONENT).colorSet();
        }

        if (item.is(BooyahItems.STAMPER_WHITE)) return WHITE;
        if (item.is(BooyahItems.STAMPER_LIGHT_GRAY)) return LIGHT_GRAY;
        if (item.is(BooyahItems.STAMPER_GRAY)) return GRAY;
        if (item.is(BooyahItems.STAMPER_BLACK)) return BLACK;
        if (item.is(BooyahItems.STAMPER_BROWN)) return BROWN;
        if (item.is(BooyahItems.STAMPER_RED)) return RED;
        if (item.is(BooyahItems.STAMPER_ORANGE)) return ORANGE;
        if (item.is(BooyahItems.STAMPER_YELLOW)) return YELLOW;
        if (item.is(BooyahItems.STAMPER_LIME)) return LIME;
        if (item.is(BooyahItems.STAMPER_GREEN)) return GREEN;
        if (item.is(BooyahItems.STAMPER_CYAN)) return CYAN;
        if (item.is(BooyahItems.STAMPER_LIGHT_BLUE)) return LIGHT_BLUE;
        if (item.is(BooyahItems.STAMPER_BLUE)) return BLUE;
        if (item.is(BooyahItems.STAMPER_PURPLE)) return PURPLE;
        if (item.is(BooyahItems.STAMPER_MAGENTA)) return MAGENTA;
        if (item.is(BooyahItems.STAMPER_PINK)) return PINK;

        return WHITE;
    }

}
