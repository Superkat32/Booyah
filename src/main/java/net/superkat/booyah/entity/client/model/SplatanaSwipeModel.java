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

    public SplatanaSwipeModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutCull);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(17, 54).addBox(-3.0F, -0.5F, -5.75F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 57).addBox(-7.0F, -0.5F, -4.75F, 14.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 42).addBox(-11.0F, -0.5F, -3.75F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 42).addBox(7.0F, -0.5F, -3.75F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 42).addBox(11.0F, -0.5F, -2.75F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 42).addBox(-14.0F, -0.5F, -2.75F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 57).addBox(-15.0F, -0.5F, -1.75F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 57).addBox(14.0F, -0.5F, -1.75F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(15.0F, -0.5F, -0.75F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(-16.0F, -0.5F, -0.75F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.5F, -1.25F));

        PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(28, 29).addBox(-3.0F, -0.5F, -5.75F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -4.75F, 14.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-11.0F, -0.5F, -3.75F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(7.0F, -0.5F, -3.75F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 7).addBox(11.0F, -0.5F, -2.75F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 14).addBox(-14.0F, -0.5F, -2.75F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(-15.0F, -0.5F, -1.75F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(14, 29).addBox(14.0F, -0.5F, -1.75F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(15.0F, -0.5F, -0.75F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(16, 21).addBox(-16.0F, -0.5F, -0.75F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.5F, 0.75F));

        PartDefinition bone3 = partdefinition.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(28, 29).addBox(-3.0F, -0.5F, -5.75F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -4.75F, 14.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-11.0F, -0.5F, -3.75F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(7.0F, -0.5F, -3.75F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 7).addBox(11.0F, -0.5F, -2.75F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 14).addBox(-14.0F, -0.5F, -2.75F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(-15.0F, -0.5F, -1.75F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(14, 29).addBox(14.0F, -0.5F, -1.75F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(15.0F, -0.5F, -0.75F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(16, 21).addBox(-16.0F, -0.5F, -0.75F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.5F, 0.75F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
