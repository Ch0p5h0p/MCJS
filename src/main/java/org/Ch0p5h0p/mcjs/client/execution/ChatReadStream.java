package org.Ch0p5h0p.mcjs.client.execution;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatReadStream extends InputStream {
    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private byte[] buffer = null;
    private int index = 0;
    private final Runnable notifyPrompt;

    public ChatReadStream(Runnable notifyPrompt) {
        this.notifyPrompt = notifyPrompt;
    }

    @Override
    public int read() throws IOException {
        if (buffer == null || index >= buffer.length) {
            if (notifyPrompt != null) {
                notifyPrompt.run();
            }

            try {
                String chatMessage = queue.take();

                buffer = (chatMessage + "\n").getBytes(StandardCharsets.UTF_8);
                index = 0;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1;
            }
        }
        return buffer[index++] & 0xFF;
    }

    public void clear() {
        queue.clear();
        buffer = null;
        index = 0;
    }

    public void provideInput(String input) {
        queue.offer(input);
    }
}
