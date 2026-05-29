package org.Ch0p5h0p.mcjs.client.execution.libraries;

import com.jcraft.jorbis.Block;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import io.github.stefanrichterhuber.quickjs.QuickJSContext;
import net.fabricmc.fabric.mixin.networking.client.accessor.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Stdlib implements Lib {
    @Override
    public void init(QuickJSContext ctx) {

        // chat API
        Map<String, Object> chat = new HashMap<>();
        chat.put("send", (Consumer<String>) (text) -> {
            Minecraft.getInstance().getConnection().sendChat(text);
        });
        chat.put("log", (Consumer<String>) (text) -> {
            Minecraft.getInstance().player.displayClientMessage(Component.literal(text), false);
        });

        ctx.setGlobal("chat", chat);

        /*TODO: implement stuff for
           [ ] interaction (interact, place, attack)
           [ ] worlds (entities and physical players)
           [ ] servers (server players and networking)
        * */

        /* TODO: Implement a new function call thing so we can get away from supplier and comsumer.
            @FunctionalInterface
            public interface FunctionObj {
                void execute();
            }
            While we're at it, potentially have some form of way to have variable args.
        *  */

        // interaction API
        Map<String, Object> interaction = new HashMap<>();
        interaction.put("attack", (Supplier<String>) () -> {
            Minecraft client = Minecraft.getInstance();

            if (client.player == null) return null;
            HitResult hit = client.hitResult;
            InteractionHand hand = InteractionHand.MAIN_HAND;

            switch (hit.getType()) {
                case ENTITY:
                    client.gameMode.attack(
                            client.player,
                            ((EntityHitResult) hit).getEntity()
                    );
                    return "ENTITY";
                case BLOCK:
                    BlockHitResult blockHit = (BlockHitResult) hit;
                    client.gameMode.startDestroyBlock(
                            blockHit.getBlockPos(),
                            blockHit.getDirection()
                    );
                    return "BLOCK";
                case MISS:
                    client.player.swing(hand);
                    return "MISS";
            }

            return null;
        });
        interaction.put("interact", (Supplier<Void>) () -> {
            Minecraft client = Minecraft.getInstance();

            if (client.player != null) {
                InteractionHand hand = InteractionHand.MAIN_HAND;
                assert client.gameMode != null;
                client.gameMode.useItem(client.player, hand);
                client.player.swing(hand);
            }
            return null;
        });

        ctx.setGlobal("player", interaction);
    }

    @Override
    public String name() {
        return "STDLIB";
    }

    @Override
    public String docs() {
        return """
                VARIABLES:
                - username -> string : contains the active player's username
                
                FUNCTIONS:
                > chat
                - send(text) -> void : sends a message to the server
                - log(text)  -> void : logs a message to chat (private)
                
                > player
                - interact() -> void : interact with a block/place a block
                - attack()   -> void : attack (duh :P)
                
                NOTE: interact only works for things like lava buckets right now
                
                """;
    }
}
