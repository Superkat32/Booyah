package net.superkat.booyah.entity.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.entity.client.state.SplatanaSwipeRenderState;

public class SplatanaSwipeModel extends EntityModel<SplatanaSwipeRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Booyah.id("splatana_slash"), "main");
    private final ModelPart bone;

	public SplatanaSwipeModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutCull);
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, 0.7F, 16.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-6.0F, 0.0F, 2.7F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(-6.0F, 0.0F, -1.3F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-4.0F, 0.0F, -2.3F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.0F, 0.0F, -3.3F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -1.7F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(SplatanaSwipeRenderState state) {
        super.setupAnim(state);
    }
}
