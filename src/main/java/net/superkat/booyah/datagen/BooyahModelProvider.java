package net.superkat.booyah.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.item.Item;
import net.superkat.booyah.item.BooyahItems;

public class BooyahModelProvider extends FabricModelProvider {
    public BooyahModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        this.generateModelWithFlatTexture(BooyahItems.SPLATANA_STAMPER, itemModelGenerators);
    }

    public void generateModelWithFlatTexture(Item item, ItemModelGenerators itemModelGenerators) {
        ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_in_hand"));
        itemModelGenerators.itemModelOutput.accept(item, ItemModelGenerators.createFlatModelDispatch(flatModel, inHandModel));
    }

    @Override
    public String getName() {
        return "BooyahModelProvider";
    }
}
