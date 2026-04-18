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
                .add(BooyahItems.STAMPER_PURPLE);
    }
}
