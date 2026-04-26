package net.superkat.booyah.entity.client.model;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.entity.client.animations.BalloonAnimations;
import net.superkat.booyah.entity.client.state.BalloonRenderState;

public class BalloonModel extends EntityModel<BalloonRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Booyah.id("balloon_chase"), "main");

    private final KeyframeAnimation idleAnimation;
    private final ModelPart main;
    private final ModelPart balloon;
    private final ModelPart balloon_tie;
    private final ModelPart strings;
    private final ModelPart strings_tie;
    private final ModelPart top;
    private final ModelPart bottom;
    private final ModelPart weight;

    public BalloonModel(ModelPart root) {
        super(root);
        this.main = root.getChild("main");
        this.balloon = this.main.getChild("balloon");
        this.balloon_tie = this.main.getChild("balloon_tie");
        this.strings = this.main.getChild("strings");
        this.strings_tie = this.strings.getChild("strings_tie");
        this.top = this.strings.getChild("top");
        this.bottom = this.strings.getChild("bottom");
        this.weight = this.strings.getChild("weight");
        this.idleAnimation = BalloonAnimations.IDLE.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 2.1667F, 0.0F));

        PartDefinition balloon = main.addOrReplaceChild("balloon", CubeListBuilder.create().texOffs(0, 22).addBox(-4.0F, -11.1667F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 31).addBox(-4.0F, 1.8333F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -10.1667F, -5.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition balloon_tie = main.addOrReplaceChild("balloon_tie", CubeListBuilder.create().texOffs(36, 35).addBox(-1.0F, -0.25F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 38).addBox(1.0F, 0.75F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0833F, 0.0F));

        PartDefinition strings = main.addOrReplaceChild("strings", CubeListBuilder.create(), PartPose.offset(0.3333F, 4.0F, 0.0F));

        PartDefinition strings_tie = strings.addOrReplaceChild("strings_tie", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.6667F, -0.6667F, 0.0F));

        PartDefinition top = strings.addOrReplaceChild("top", CubeListBuilder.create().texOffs(32, 22).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.3333F, -1.1667F, 0.0F));

        PartDefinition cube_r1 = top.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 34).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.6144F, 0.0F));

        PartDefinition bottom = strings.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(36, 27).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.3333F, 10.8333F, 0.0F));

        PartDefinition cube_r2 = bottom.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(36, 31).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.6144F, 0.0F));

        PartDefinition weight = strings.addOrReplaceChild("weight", CubeListBuilder.create().texOffs(36, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.3333F, 14.8333F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(BalloonRenderState state) {
        super.setupAnim(state);
        this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks);
    }
}
