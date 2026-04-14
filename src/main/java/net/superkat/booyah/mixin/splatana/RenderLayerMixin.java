package net.superkat.booyah.mixin.splatana;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderLayer.class)
public abstract class RenderLayerMixin<S extends EntityRenderState, M extends EntityModel<? super S>> {

    @Shadow
    public abstract M getParentModel();

}
