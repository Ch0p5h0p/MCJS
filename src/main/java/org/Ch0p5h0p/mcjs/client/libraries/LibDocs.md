# How to make libraries
This is simple docs for how to make libraries

## Objects
Example: chat API from STDLIB v1.0
```java
// base object
Map<String, Object> chat = new HashMap<>();

// variables
chat.put("username", Minecraft.getInstance().player.getName().getString());

// functions
chat.put("send", (Consumer<String>) (text) -> {
    Minecraft.getInstance().getConnection().sendChat(text);
});

chat.put("log", (Consumer<String>) (text) -> {
    Minecraft.getInstance().player.displayClientMessage(Component.literal(text), false);
});

// registration of the object
ctx.setGlobal("chat", chat);
```

Consumer is from the java functions library

## Functions
Awaiting demo
