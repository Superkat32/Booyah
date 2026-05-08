package net.superkat.booyah.block.client.screen;

import com.mojang.blaze3d.platform.Window;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.Button;
import dev.chailotl.bento_gui.client.elements.Label;
import dev.chailotl.bento_gui.client.elements.Panel;
import dev.chailotl.bento_gui.client.elements.TextField;
import dev.chailotl.bento_gui.client.elements.ToggleButton;
import dev.chailotl.bento_gui.client.elements.interfaces.ValueElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
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
    public Button roundYawButton;
    public ToggleButton dropRewardToggleButton;
    public Label dropRewardInfoLabel;

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
                .dimensions(true, true)
                .padding(0, 0, 8, 0)
                .build();
        Label title = Label.builder()
                .text(Component.literal("Ballooon Chase Block"))
                .dimensions(true, 8)
                .build();
        inputsPanel.addChild(title);

        Panel chainInfoPanel = Panel.builder()
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
                .text(Component.literal("Float Away Ticks (Int)").withStyle(ChatFormatting.GRAY))
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
                .alignCenter()
                .flowAxis(FlowAxis.HORIZONTAL)
                .build();
        Button autoYawButton = Button.builder()
                .text(Component.literal("Face Me!"))
                .onPress(button -> {
                    LocalPlayer player = this.minecraft.player;
                    if (player == null) return;

                    float angle = (float) Mth.atan2(player.getZ() - this.blockEntity.getBlockPos().getCenter().z(), player.getX() - this.blockEntity.getBlockPos().getCenter().x());
                    this.yawField.setValue((float) Math.toDegrees(angle) - 90);
                })
                .dimensions(60, true)
                .alignCenter()
                .build();
        this.roundYawButton = Button.builder()
                .text(Component.literal("Round 45"))
                .tooltip(Tooltip.create(Component.literal("Shift: 90, Ctrl: 22.5")))
                .onPress(button -> {
                    float roundAmount = 45f;
                    if (this.minecraft.hasShiftDown()) {
                        roundAmount = 90f;
                    } else if (this.minecraft.hasControlDown()) {
                        roundAmount = 22.5f;
                    }

                    if (this.yawField.getValue() == null) return;
                    float angle = this.yawField.getValue();
                    this.yawField.setValue(Math.round(angle / roundAmount) * roundAmount);
                })
                .dimensions(62, true)
                .alignCenter()
                .build();
        autoYawPanel.addChild(autoYawButton);
        autoYawPanel.addChild(roundYawButton);
        yawPanel.addChild(yawField);
        yawPanel.addChild(autoYawPanel);

        rotationsPanel.addChild(rotationInfoPanel);
        rotationsPanel.addChild(yawPanel);

        inputsPanel.addChild(rotationsPanel);



        Panel rewardsPanel = Panel.builder()
                .dimensions(true, true)
                .padding(0, 0, 0, 0)
                .alignCenter()
                .build();

        Panel popRewardPanel = Panel.builder()
                .dimensions(true, 20)
                .padding(0, 0, 0, -2)
                .alignCenter()
                .flowAxis(FlowAxis.HORIZONTAL)
                .build();

        Panel rewardTogglePanel = Panel.builder()
                .dimensions(20, true)
                .alignLeft()
                .build();
        this.dropRewardToggleButton = ToggleButton.ofCheckbox()
                .value(this.blockEntity.balloonEntry != null && this.blockEntity.balloonEntry.rewardItemOnPop())
                .onPress(button -> {
                    if (this.dropRewardInfoLabel != null) this.updateDynamicButtons();
                })
                .build();
        rewardTogglePanel.addChild(dropRewardToggleButton);

        Panel rewardInfoPanel = Panel.builder()
                .dimensions(236, true)
                .alignLeft()
                .build();
        Label dropRewardLabel = Label.builder()
                .text(Component.literal("Reward Item on Pop").withStyle(ChatFormatting.GRAY))
                .tooltip(Tooltip.create(Component.literal("Only applies to this balloon!")))
                .dimensions(true, 6)
                .padding(1,0, 2, 0)
                .alignLeft()
                .build();
        this.dropRewardInfoLabel = Label.builder()
                .text(Component.literal("Right-click block with item to set its reward.").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC))
                .tooltip(Tooltip.create(Component.literal("Just like shelf blocks.")))
                .dimensions(true, 6)
                .padding(1,0, 2, 0)
                .alignLeft()
                .build();
        rewardInfoPanel.addChild(dropRewardLabel);
        rewardInfoPanel.addChild(dropRewardInfoLabel);

        popRewardPanel.addChild(rewardTogglePanel);
        popRewardPanel.addChild(rewardInfoPanel);

//        Panel chainRewardsPanel = Panel.builder()
//                .dimensions(true, 20)
//                .padding(0, 0, 0, 0)
//                .alignCenter()
//                .flowAxis(FlowAxis.HORIZONTAL)
//                .build();
//
//        Panel chainRewardTogglePanel = Panel.builder()
//                .dimensions(20, true)
//                .alignLeft()
//                .build();
//        ToggleButton chainRewardToggleButton = ToggleButton.ofCheckbox()
//                .value(this.blockEntity.balloonEntry != null && this.blockEntity.balloonEntry.rewardItemOnPop())
////                .onPress(button -> {
////                    if (this.dropRewardInfoLabel != null) this.updateDynamicButtons();
////                })
//                .build();
//        chainRewardTogglePanel.addChild(chainRewardToggleButton);
//
//        Panel chainRewardInfoPanel = Panel.builder()
//                .dimensions(236, true)
//                .alignLeft()
//                .build();
//        Label chainRewardLabel = Label.builder()
//                .text(Component.literal("Reward Item on Chain Index Pop").withStyle(ChatFormatting.GRAY))
//                .tooltip(Tooltip.create(Component.literal("Only applies when all balloons in this Chain Index are popped!")))
//                .dimensions(true, 6)
//                .padding(1,0, 2, 0)
//                .alignLeft()
//                .build();
//        Label chainDropRewardInfoLabel = Label.builder()
//                .text(Component.literal("Sneak right-click any block with item to set.").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC))
//                .tooltip(Tooltip.create(Component.literal("Sets the item for all balloons with same Chain Index!")))
//                .dimensions(true, 6)
//                .padding(1,0, 2, 0)
//                .alignLeft()
//                .build();
//        chainRewardInfoPanel.addChild(chainRewardLabel);
//        chainRewardInfoPanel.addChild(chainDropRewardInfoLabel);
//
//        chainRewardsPanel.addChild(chainRewardTogglePanel);
//        chainRewardsPanel.addChild(chainRewardInfoPanel);


        rewardsPanel.addChild(popRewardPanel);
//        rewardsPanel.addChild(chainRewardsPanel);
        inputsPanel.addChild(rewardsPanel);



        Panel buttonsPanel = Panel.builder()
                .dimensions(true, 44)
                .alignBottom()
                .alignCenter()
                .padding(0, 0, 0, 16)
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

        this.updateDynamicButtons();
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isConfirmation()) {
            this.save();
        }

        this.updateDynamicButtons();
        return super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        this.updateDynamicButtons();
        return super.keyReleased(event);
    }

    public void updateDynamicButtons() {
        if (this.minecraft.hasShiftDown() || this.minecraft.hasControlDown()) {
            if (this.minecraft.hasShiftDown()) {
                this.roundYawButton.setText(Component.literal("Round 90"));
            } else if (this.minecraft.hasControlDown()) {
                this.roundYawButton.setText(Component.literal("Round 22.5"));
            }
        } else {
            this.roundYawButton.setText(Component.literal("Round 45"));
        }

//        MutableComponent rewardInfoText = Component.literal("Right-Click block with item in hand to set reward.");
//        if (this.dropRewardToggleButton.getValue()) {
//            rewardInfoText.withStyle(ChatFormatting.GRAY);
//        } else {
//            rewardInfoText.withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.STRIKETHROUGH);
//        }
//        this.dropRewardInfoLabel.setText(rewardInfoText);
    }

    public void save() {
        String chainId = this.chainIdField.getValue();
        int entryIndex = this.getFieldValue(this.chainIndexField, 0);
        int spawnDelayTicks = this.getFieldValue(this.spawnDelayField, 0);
        int floatAwayTicks = this.getFieldValue(this.floatAwayField, 300);
        float balloonYaw = this.getFieldValue(this.yawField, 0f);
        boolean rewardItemOnPop = this.getFieldValue(this.dropRewardToggleButton, false);
        ItemStack popReward = (this.blockEntity.balloonEntry == null || !rewardItemOnPop)
                ? ItemStack.EMPTY : this.blockEntity.balloonEntry.popReward();

        ClientPlayNetworking.send(
                new C2SBalloonChaseBlockUpdatePacket(chainId, new BalloonEntry(
                        this.blockEntity.getBlockPos(), entryIndex,
                        spawnDelayTicks, floatAwayTicks,
                        balloonYaw,
                        rewardItemOnPop, popReward
                ))
        );

//        Component chainIdName = Component.literal(chainId).withStyle(ChatFormatting.AQUA);
//        MutableComponent message = Component.literal("Saved Balloon Chase Block");
//        if (!chainId.isBlank()) {
//            message.append(" ").append(chainIdName);
//        }
//        message.append("!");
//        this.minecraft.player.sendSystemMessage(message);

        this.onClose();
    }

    private <T> T getFieldValue(ValueElement<T> field, T defaultValue) {
        return field.getValue() == null ? defaultValue : field.getValue();
    }
}
