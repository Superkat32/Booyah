package net.superkat.booyah.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.item.Item;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.item.BooyahItems;
import net.superkat.booyah.item.client.SplatanaCharge;

import java.util.Optional;

public class BooyahModelProvider extends FabricModelProvider {
    public static final ModelTemplate SPLATANA_STAMPER_HAND_MODEL = new ModelTemplate(
            Optional.of(Booyah.id("item/template/splatana_stamper_template")), Optional.empty(), TextureSlot.LAYER0
    );
    public static final ModelTemplate SPLATANA_STAMPER_HAND_ANIMATED_MODEL = new ModelTemplate(
            Optional.of(Booyah.id("item/template/splatana_stamper_animated_template")), Optional.empty(), TextureSlot.LAYER0
    );

    public BooyahModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        this.generateModelWithFlatTexture(BooyahItems.STAMPER_WHITE, itemModelGenerators);
        this.generateModelWithFlatTexture(BooyahItems.STAMPER_PURPLE, itemModelGenerators);
    }

    public void generateModelWithFlatTexture(Item item, ItemModelGenerators itemModelGenerators) {
        ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
//        ItemModel.Unbaked inHandModelNormal = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_hand"));
//        ItemModel.Unbaked inHandAnimatedModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_hand_animated"));
        ItemModel.Unbaked inHandModelNormal = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(item, "_hand", SPLATANA_STAMPER_HAND_MODEL));
        ItemModel.Unbaked inHandAnimatedModel = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(item, "_hand_animated", SPLATANA_STAMPER_HAND_ANIMATED_MODEL));

        ItemModel.Unbaked inHandModel = ItemModelUtils.conditional(new SplatanaCharge(), inHandAnimatedModel, inHandModelNormal);
        itemModelGenerators.itemModelOutput.accept(item, ItemModelGenerators.createFlatModelDispatch(flatModel, inHandModel));
    }

    @Override
    public String getName() {
        return "BooyahModelProvider";
    }
}
