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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.network.packets.balloon.C2SBalloonChaseBlockUpdatePacket;

@Environment(EnvType.CLIENT)
public class BalloonChaseEditBlockScreen extends Screen {
    public final BalloonChaseBlockEntity blockEntity;
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
                .spacing(1)
                .build();

        Label title = Label.builder()
                .text(Component.literal("Ballooon Chase Block"))
                .dimensions(true, 32)
                .build();

        Panel chainIdPanel = Panel.builder()
                .dimensions(true, 128)
                .alignCenter()
                .build();

        Label chainIdLabel = Label.builder()
                .text(Component.literal("Balloon Chase Chain ID").withStyle(ChatFormatting.GRAY))
                .dimensions(true, 8)
                .maxWidth(200)
                .alignLeft()
                .build();

        TextField<String> chainIdText = TextField.ofString()
                .text(this.blockEntity.getChainId())
                .dimensions(true, 24)
                .maxWidth(200)
                .alignCenter()
                .build();

        Label chainIndexLabel = Label.builder()
                .text(Component.literal("Balloon Chase Chain Index").withStyle(ChatFormatting.GRAY))
                .dimensions(true, 8)
                .maxWidth(200)
                .alignLeft()
                .build();

        TextField<Integer> chainIndexText = TextField.ofInteger()
                .text(this.blockEntity.getChainIndex())
                .dimensions(true, 24)
                .maxWidth(200)
                .alignCenter()
                .build();

        chainIdPanel.addChild(chainIdLabel);
        chainIdPanel.addChild(chainIdText);
        chainIdPanel.addChild(chainIndexLabel);
        chainIdPanel.addChild(chainIndexText);

        Panel buttonsPanel = Panel.builder()
                .dimensions(true, true)
                .padding(0, 24)
                .alignCenter()
                .alignBottom()
                .flowAxis(FlowAxis.HORIZONTAL)
                .build();

        Button saveButton = Button.builder()
                .text(Component.literal("Save"))
                .onPress(self -> {
                    this.save(chainIdText.getValue(), chainIndexText.getValue());
                    this.onClose();
                })
                .build();

        Button cancelButton = Button.builder()
                .text(Component.literal("Cancel"))
                .onPress(self -> this.onClose())
                .build();

        buttonsPanel.addChild(saveButton);
        buttonsPanel.addChild(cancelButton);

        root.addChild(title);
        root.addChild(chainIdPanel);
        root.addChild(buttonsPanel);

        addRenderableWidget(root);
    }

    public void save(String chainId, Integer entryIndex) {
        ClientPlayNetworking.send(new C2SBalloonChaseBlockUpdatePacket(this.blockEntity.getBlockPos(), chainId, entryIndex));
    }
}
