package org.Ch0p5h0p.mcjs.client.scripting;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptManager {
    private static final String MODID = "mcpython";

    public static Path getModFolder() {
        Path modFolder = FabricLoader.getInstance().getConfigDir().resolve(MODID);

        try {
            if (Files.notExists(modFolder)) {
                Files.createDirectories(modFolder);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return modFolder;
    }

    public static File getFile(String name) {
        Path fileTarget = getModFolder().resolve(name);
        return fileTarget.toFile();
    }

    public static void openFile(String name) {
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(new ScriptingScreen(getFile(name)));
        });
    }

    public static void saveToFile(String data, File file) {
        try {
            Files.writeString(file.toPath(), data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(File file) {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<String> getFileNames() throws IOException {
        List<Path> files = Files.list(getModFolder()).collect(Collectors.toList());
        List<String> names = files.stream().map((file) -> file.getFileName().toString()).toList();
        return names;
    }

    public static String listFiles() throws IOException {
        List<String> files = getFileNames();
        StringBuilder sb = new StringBuilder();

        for (String file : files) {
            sb.append(file + "\n");
        }
        return sb.toString();
    }
}
