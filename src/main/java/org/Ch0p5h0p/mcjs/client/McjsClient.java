package org.Ch0p5h0p.mcjs.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.Ch0p5h0p.mcjs.client.execution.JSExecutor;
import org.Ch0p5h0p.mcjs.client.execution.JSREPL;
import org.Ch0p5h0p.mcjs.client.libraries.LibCollector;
import org.Ch0p5h0p.mcjs.client.libraries.LibDocsScreen;
import org.Ch0p5h0p.mcjs.client.libraries.Stdlib;
import org.Ch0p5h0p.mcjs.client.scripting.ScriptManager;

import java.io.IOException;
import java.util.List;

public class McjsClient implements ClientModInitializer {

    JSREPL jsrepl;

    @Override
    public void onInitializeClient() {

        LibCollector.register(new Stdlib());

        this.jsrepl = new JSREPL();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("codeWindow")
                    .then(ClientCommandManager.argument("fileName", StringArgumentType.string())
                                    .suggests((ctx, builder) -> {
                                        try {
                                            List<String> filenames = ScriptManager.getFileNames();
                                            for (String name: filenames) {
                                                builder.suggest(name);
                                            }
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }

                                        return builder.buildFuture();
                                    })
                                    .executes(context -> {
                                        ScriptManager.openFile(StringArgumentType.getString(context, "fileName"));
                                        return 1;
                                    })
                            )

            );
            dispatcher.register(ClientCommandManager.literal("delFile")
                    .then(ClientCommandManager.argument("fileName", StringArgumentType.string())
                            .suggests((ctx, builder) -> {
                                try {
                                    List<String> filenames = ScriptManager.getFileNames();
                                    for (String name: filenames) {
                                        builder.suggest(name);
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                ScriptManager.deleteFile(ScriptManager.getFile(StringArgumentType.getString(context, "fileName")));
                                return 1;
                            })
                    )

            );
            dispatcher.register(ClientCommandManager.literal("listFiles")
                    .executes(context -> {
                        try {
                            Minecraft.getInstance().player.displayClientMessage(Component.literal(ScriptManager.listFiles()), false);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return 1;
                    })
            );
            dispatcher.register(ClientCommandManager.literal("runFile")
                    .then(ClientCommandManager.argument("fileName", StringArgumentType.string())
                            .suggests((ctx, builder) -> {
                                try {
                                    List<String> filenames = ScriptManager.getFileNames();
                                    for (String name: filenames) {
                                        builder.suggest(name);
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                try {
                                    String output = JSExecutor.runScript(ScriptManager.getFile(
                                            StringArgumentType.getString(context, "fileName")
                                    ));
                                    Minecraft.getInstance().player.displayClientMessage(Component.literal(output), false);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Minecraft.getInstance().player.displayClientMessage(
                                            Component.literal("Execution failed. See game logs for more info")
                                                    .withStyle(ChatFormatting.RED),
                                            false
                                    );
                                    return 0;
                                }
                                return 1;
                            })
                    )

            );
            dispatcher.register(ClientCommandManager.literal("repl")
                    .then(ClientCommandManager.argument("code", StringArgumentType.greedyString())
                            .executes(context -> {
                                String output = jsrepl.runCode(
                                        StringArgumentType.getString(context, "code")
                                );
                                Minecraft.getInstance().player.displayClientMessage(Component.literal(output), false);
                                return 1;
                            })
                    )

            );

            dispatcher.register(ClientCommandManager.literal("libraries")
                    .executes(context -> {
                        Minecraft.getInstance().player.displayClientMessage(Component.literal(LibCollector.getLibNames()), false);
                        return 1;
                    })
            );
            dispatcher.register(ClientCommandManager.literal("libDocs")
                    .then(ClientCommandManager.argument("library name", StringArgumentType.string())
                            .suggests((ctx, builder) -> {
                                List<String> libnames = LibCollector.getLibList();
                                for (String name : libnames) {
                                    builder.suggest(name);
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                String libname = StringArgumentType.getString(context, "library name");
                                Minecraft.getInstance().execute(() -> {
                                    Minecraft.getInstance().setScreen(new LibDocsScreen(
                                            libname,
                                            LibCollector.getLibDoc(libname)
                                    ));
                                });
                                /*Minecraft.getInstance().player.displayClientMessage(
                                        Component.literal(
                                                LibCollector.getLibDoc(StringArgumentType.getString(context, "library name"))
                                        ), false);*/
                                return 1;
                            })
                    )
            );

            dispatcher.register(ClientCommandManager.literal("mcjs-help")
                    .executes(context -> {
                        Minecraft.getInstance().player.displayClientMessage(Component.literal(helpMsg()), false);
                        return 1;
                    })
            );

        });
    }

    private String helpMsg() {
        return """
                MCJS: A coding mod for Minecraft
                - By Ch0p5h0p
                
                COMMANDS:
                /codeWindow <filename>  : open or create a file
                /delFile <filename>     : delete a file
                /listFiles              : list all available files
                /runFile <filename>     : run a JS file
                /repl <code>            : run some code in the repl
                /libraries              : list all libraries loaded
                /libDocs <library name> : get the documentation for a library
                /mcjs-help              : display this message
                """;
    }
}
