package net.superkat.booyah.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.superkat.booyah.item.BooyahItems;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class BooyahItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public BooyahItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        valueLookupBuilder(BooyahItems.SPLATANA_TAG)
                .add(BooyahItems.STAMPER_WHITE)
                .add(BooyahItems.STAMPER_LIGHT_GRAY)
                .add(BooyahItems.STAMPER_GRAY)
                .add(BooyahItems.STAMPER_BLACK)
                .add(BooyahItems.STAMPER_BROWN)
                .add(BooyahItems.STAMPER_RED)
                .add(BooyahItems.STAMPER_ORANGE)
                .add(BooyahItems.STAMPER_YELLOW)
                .add(BooyahItems.STAMPER_LIME)
                .add(BooyahItems.STAMPER_GREEN)
                .add(BooyahItems.STAMPER_CYAN)
                .add(BooyahItems.STAMPER_LIGHT_BLUE)
                .add(BooyahItems.STAMPER_BLUE)
                .add(BooyahItems.STAMPER_PURPLE)
                .add(BooyahItems.STAMPER_MAGENTA)
                .add(BooyahItems.STAMPER_PINK)
                .add(BooyahItems.STAMPER_TRANS);
    }
}
