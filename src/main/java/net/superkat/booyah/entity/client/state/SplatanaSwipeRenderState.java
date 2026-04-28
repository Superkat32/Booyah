package net.superkat.booyah.entity.client.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.CommonColors;

public class SplatanaSwipeRenderState extends EntityRenderState {
    public float xRot;
    public float yRot;
    public float zRot;
    public int mainColor = CommonColors.WHITE;

    public int extraColorA = CommonColors.RED;
    public float extraModelAX = 0;
    public float extraModelAY = 0;

    public int extraColorB = CommonColors.GREEN;
    public float extraModelBX = 0;
    public float extraModelBY = 0;

    public int extraColorC = CommonColors.BLUE;
    public float extraModelCX = 0;
    public float extraModelCY = 0;

}
