package org.Ch0p5h0p.mcjs.client.execution.libraries;

import io.github.stefanrichterhuber.quickjs.QuickJSContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class LibCollector {
    private static Map<String, Lib> loadedLibs = new HashMap<>();

    public static void register(Lib lib) {
        if (loadedLibs.containsKey(lib.name())) {
            sendWarn("Library with name " + lib.name() + " already registered, skipped.");
            return;
        }
        if (lib.name().contains(" ")) {
            sendWarn("Library \""+lib.name()+"\" contains spaces in the name, skipped.");
            return;
        }
        loadedLibs.put(lib.name(), lib);
    }

    public static String getLibNames() {
        StringBuilder sb = new StringBuilder("LIBRARIES: ");
        for (String name : loadedLibs.keySet()) {
            sb.append(name+" ");
        }
        return sb.toString();
    }

    public static String getLibDoc(String libname) {
        if (loadedLibs.containsKey(libname)) {
            return loadedLibs.get(libname).docs();
        } else {
            return "Library doesn't exist.";
        }
    }

    private static void sendWarn(String text) {
        Minecraft.getInstance().player.displayClientMessage(
                Component.literal("WARN: "+ text).withStyle(ChatFormatting.YELLOW),
                false
        );
    }

    public static void addLibraries(QuickJSContext ctx) {
        for (Lib lib : loadedLibs.values()) {
            lib.init(ctx);
        }
    }
}
