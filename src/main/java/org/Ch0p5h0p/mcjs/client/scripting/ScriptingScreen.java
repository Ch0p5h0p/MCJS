package org.Ch0p5h0p.mcjs.client.scripting;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ScriptingScreen extends Screen {

    private final File file;
    private MultiLineEditBox textBox;

    public ScriptingScreen(File file) {
        super(Component.literal(file.getName()));
        this.file = file;
    }

    @Override
    protected void init() {

        MultiLineEditBox.Builder builder = new MultiLineEditBox.Builder();
        int boxHeight = (int) ((this.height*0.75) - (this.height*0.25));
        int boxWidth = (int) ((this.width*0.75) - (this.width*0.25));

        builder.setX((int) (this.width*0.25));
        builder.setY((int) (this.height*0.25));
        builder.setPlaceholder(Component.literal("Type here..."));
        builder.setTextShadow(false);

        textBox = builder.build(this.font, boxWidth, boxHeight, Component.literal("Code Window"));

        try {
            textBox.setValue(Files.readString(this.file.toPath()));
        } catch (IOException ignored) {}

        textBox.setLineLimit(1000);

        Button.Builder button = new Button.Builder(
                Component.literal("Save"),
                btn -> {
                    ScriptManager.saveToFile(
                            textBox.getValue(),
                            this.file
                    );
                    this.onClose();
                }
        );
        button.size(75, 20);
        button.pos((this.width/2)-(75/2), (int) (this.height*0.78));

        StringWidget titleWidget = new StringWidget(this.title, this.font);
        titleWidget.setPosition((int) (this.width*0.25), (int) ((this.height*0.25)-12));

        this.addRenderableWidget(titleWidget);
        this.addRenderableWidget(textBox);
        this.addRenderableWidget(button.build());
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        if (keyCode == GLFW.GLFW_KEY_TAB) {
            if (this.textBox != null && this.textBox.isFocused()) {
                this.textBox.charTyped(new CharacterEvent(' ', 0));
                this.textBox.charTyped(new CharacterEvent(' ', 0));
                return true;
            }
        }

        return super.keyPressed(event);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
