package org.Ch0p5h0p.mcjs.client.libraries;

import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class LibDocsScreen extends Screen {
    String libName;
    String libDocs;

    public LibDocsScreen(String name, String docs) {
        super(Component.literal(name));
        this.libName = name;
        this.libDocs = docs;
    }

    @Override
    protected void init() {
        MultiLineTextWidget docsPanel = new MultiLineTextWidget(Component.literal("--- "+this.libName+" docs ---\n\n"+this.libDocs+"\n\n\n\n[ESC to exit]"), this.font);
        //docsPanel.setPosition();
        //docsPanel.setSize(this.width, this.height);

        this.addRenderableWidget(docsPanel);
    }

    @Override
    public void tick() {super.tick();}
}
