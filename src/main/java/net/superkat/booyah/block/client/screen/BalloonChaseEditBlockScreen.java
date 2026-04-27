package net.superkat.booyah.block.client.screen;

import com.mojang.blaze3d.platform.Window;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.Button;
import dev.chailotl.bento_gui.client.elements.Label;
import dev.chailotl.bento_gui.client.elements.Panel;
import dev.chailotl.bento_gui.client.elements.TextField;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.superkat.booyah.balloon.BalloonEntry;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.network.packets.balloon.C2SBalloonChaseBlockUpdatePacket;

@Environment(EnvType.CLIENT)
public class BalloonChaseEditBlockScreen extends Screen {
    public final BalloonChaseBlockEntity blockEntity;

    public TextField<String> chainIdField;
    public TextField<Integer> chainIndexField;
    public TextField<Integer> spawnDelayField;
    public TextField<Integer> floatAwayField;
    public TextField<Float> yawField;

    public BalloonChaseEditBlockScreen(BalloonChaseBlockEntity blockEntity) {
        super(Component.literal("Balloon Chase Block Edit Screen"));
        this.blockEntity = blockEntity;
    }

    @Override
    protected void init() {
        Window window = this.minecraft.getWindow();
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();

        Panel root = Panel.builder()
                .dimensions(width, height)
                .spacing(0)
                .build();

        Panel inputsPanel = Panel.builder()
                .tooltip(Tooltip.create(Component.literal("inputs")))
                .dimensions(true, true)
                .padding(0, 0, 8, 0)
                .build();
        Label title = Label.builder()
                .text(Component.literal("Ballooon Chase Block"))
                .dimensions(true, 8)
                .build();
        inputsPanel.addChild(title);

        Panel chainInfoPanel = Panel.builder()
                .tooltip(Tooltip.create(Component.literal("chainInfo")))
                .dimensions(true, 44)
                .alignCenter()
                .padding(0, 4)
                .flowAxis(FlowAxis.HORIZONTAL)
                .build();

        Panel chainIdPanel = Panel.builder()
                .dimensions(128, true)
                .alignCenter()
                .build();
        Label chainIdLabel = Label.builder()
                .text(Component.literal("Chain ID (String)").withStyle(ChatFormatting.GRAY))
                .alignLeft()
                .dimensions(true, 6)
                .padding(0,0, 2, 0)
                .build();
        this.chainIdField = TextField.ofString()
                .text(this.blockEntity.getChainId())
                .dimensions(true, 24)
                .alignLeft()
                .build();
        chainIdPanel.addChild(chainIdLabel);
        chainIdPanel.addChild(chainIdField);

        Panel chainIndexPanel = Panel.builder()
                .dimensions(128, true)
                .alignCenter()
                .build();
        Label chainIndexLabel = Label.builder()
                .text(Component.literal("Chain Index (Int)").withStyle(ChatFormatting.GRAY))
                .alignLeft()
                .dimensions(true, 6)
                .padding(0,0, 2, 0)
                .build();
        this.chainIndexField = TextField.ofInteger()
                .text(this.blockEntity.getChainIndexString())
                .dimensions(true, 24)
                .alignLeft()
                .build();
        chainIndexPanel.addChild(chainIndexLabel);
        chainIndexPanel.addChild(chainIndexField);

        chainInfoPanel.addChild(chainIdPanel);
        chainInfoPanel.addChild(chainIndexPanel);

        inputsPanel.addChild(chainInfoPanel);



        Panel timingsPanel = Panel.builder()
                .dimensions(true, 36)
                .padding(0, 0, 0, 0)
                .alignCenter()
                .alignTop()
                .flowAxis(FlowAxis.HORIZONTAL)
                .build();

        Panel spawnDelayPanel = Panel.builder()
                .dimensions(128, true)
                .alignCenter()
                .build();
        Label spawnDelayLabel = Label.builder()
                .text(Component.literal("Spawn Delay Ticks (Int)").withStyle(ChatFormatting.GRAY))
                .alignLeft()
                .dimensions(true, 6)
                .padding(0,0, 2, 0)
                .build();
        this.spawnDelayField = TextField.ofInteger()
                .text(this.blockEntity.getSpawnDelayString())
                .dimensions(true, 24)
                .alignLeft()
                .build();
        spawnDelayPanel.addChild(spawnDelayLabel);
        spawnDelayPanel.addChild(spawnDelayField);

        Panel floatAwayPanel = Panel.builder()
                .dimensions(128, true)
                .alignCenter()
                .build();
        Label floatAwayLabel = Label.builder()
                .text(Component.literal("Float Away Ticks (Int > 0)").withStyle(ChatFormatting.GRAY))
                .alignLeft()
                .dimensions(true, 6)
                .padding(0,0, 2, 0)
                .build();
        this.floatAwayField = TextField.ofInteger()
                .text(this.blockEntity.getFloatAwayString())
                .dimensions(true, 24)
                .alignLeft()
                .build();
        floatAwayPanel.addChild(floatAwayLabel);
        floatAwayPanel.addChild(floatAwayField);

        timingsPanel.addChild(spawnDelayPanel);
        timingsPanel.addChild(floatAwayPanel);

        inputsPanel.addChild(timingsPanel);



        Panel rotationsPanel = Panel.builder()
                .dimensions(true, 46)
                .padding(0, 0, 0, 0)
                .alignCenter()
                .alignTop()
                .build();

        Panel rotationInfoPanel = Panel.builder()
                .dimensions(260, true)
                .padding(0, 0, 0, 0)
                .alignCenter()
                .alignTop()
                .build();
        Label yawLabel = Label.builder()
                .text(Component.literal("Yaw (Float -180 - 180)").withStyle(ChatFormatting.GRAY))
                .alignLeft()
                .dimensions(true, 0)
                .padding(0,0, 8, 0)
                .build();
        Label yawTipLabel = Label.builder()
                .text(Component.literal("N=+-180, E=-90, S=+-0, W=+90").withStyle(ChatFormatting.GRAY))
                .alignLeft()
                .alignTop()
                .dimensions(true, 6)
                .padding(0, 0, 4, 0)
                .build();
        rotationInfoPanel.addChild(yawLabel);
        rotationInfoPanel.addChild(yawTipLabel);

        Panel yawPanel = Panel.builder()
                .dimensions(true, 24)
                .alignCenter()
                .alignTop()
                .padding(0, 0, 0, 0)
                .flowAxis(FlowAxis.HORIZONTAL)
                .build();
        this.yawField = TextField.ofFloat()
                .text(this.blockEntity.getYawString())
                .dimensions(128, true)
                .alignLeft()
                .build();
        Panel autoYawPanel = Panel.builder()
                .dimensions(128, true)
                .alignRight()
                .build();
        Button autoYawButton = Button.builder()
                .text(Component.literal("Face Me!"))
                .dimensions(64, true)
                .alignCenter()
                .build();
        autoYawPanel.addChild(autoYawButton);
        yawPanel.addChild(yawField);
        yawPanel.addChild(autoYawPanel);

        rotationsPanel.addChild(rotationInfoPanel);
        rotationsPanel.addChild(yawPanel);

        inputsPanel.addChild(rotationsPanel);



        Panel buttonsPanel = Panel.builder()
                .dimensions(true, 44)
                .alignBottom()
                .alignCenter()
                .padding(0, 0, 0, 24)
                .flowAxis(FlowAxis.HORIZONTAL)
                .build();
        Button saveButton = Button.builder()
                .text(Component.literal("Save!"))
                .onPress(button -> this.save())
                .dimensions(128, 20)
                .alignCenter()
                .build();
        Button cancelButton = Button.builder()
                .text(Component.literal("Cancel..."))
                .onPress(button -> this.onClose())
                .dimensions(128, 20)
                .alignCenter()
                .build();
        buttonsPanel.addChild(saveButton);
        buttonsPanel.addChild(cancelButton);



        root.addChild(inputsPanel);
        root.addChild(buttonsPanel);
        addRenderableWidget(root);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isConfirmation()) {
            this.save();
        }
        return super.keyPressed(event);
    }

    public void save() {
        String chainId = this.chainIdField.getValue();
        int entryIndex = this.getFieldValue(this.chainIndexField, 0);
        int spawnDelayTicks = this.getFieldValue(this.spawnDelayField, 0);
        int floatAwayTicks = this.getFieldValue(this.floatAwayField, 300);
        float balloonYaw = this.getFieldValue(this.yawField, 0f);

        ClientPlayNetworking.send(
                new C2SBalloonChaseBlockUpdatePacket(chainId, new BalloonEntry(
                        this.blockEntity.getBlockPos(), entryIndex, spawnDelayTicks, floatAwayTicks, balloonYaw
                ))
        );

        this.minecraft.player.sendSystemMessage(Component.literal("Saved Balloon Chase Block!"));

        this.onClose();
    }

    private <T> T getFieldValue(TextField<T> field, T defaultValue) {
        return field.getValue() == null ? defaultValue : field.getValue();
    }
}
