package net.superkat.booyah.entity.client.renderer;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.superkat.booyah.entity.SplatanaDroplet;
import net.superkat.booyah.entity.client.state.SplatanaDropletRenderState;

public class SplatanaDropletRenderer extends EntityRenderer<SplatanaDroplet, SplatanaDropletRenderState> {
    public SplatanaDropletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(SplatanaDroplet entity, Frustum culler, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    public SplatanaDropletRenderState createRenderState() {
        return new SplatanaDropletRenderState();
    }
}
