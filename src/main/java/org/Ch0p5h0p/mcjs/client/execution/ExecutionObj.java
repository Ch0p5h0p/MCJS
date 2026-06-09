package org.Ch0p5h0p.mcjs.client.execution;

import io.github.stefanrichterhuber.quickjs.QuickJSContext;
import io.github.stefanrichterhuber.quickjs.QuickJSRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.Ch0p5h0p.mcjs.client.libraries.LibCollector;

public class ExecutionObj {
    private String fileName;
    private String code;
    private ChatReadStream inputStream;

    public ExecutionObj(String fileName, String code) {
        this.fileName = fileName;
        this.code = code;
        inputStream = new ChatReadStream(() -> {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.displayClientMessage(Component.literal(fileName+" awaiting input: "), false);
        });
    }

    public String run() throws Exception {
        inputStream.clear();
        QuickJSContext ctx = null;


        try (QuickJSRuntime runtime = new QuickJSRuntime()){
            ctx = runtime.createContext();
            LibCollector.addLibraries(ctx);

            Object result = ctx.eval(code);

            return result == null ? "undefined" : result.toString();
        } catch (Throwable t) {
            t.printStackTrace();

            return "ERROR EXECUTING SCRIPT: " + t.getClass().getName() + ": " + t.getMessage();
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }
    }
}
