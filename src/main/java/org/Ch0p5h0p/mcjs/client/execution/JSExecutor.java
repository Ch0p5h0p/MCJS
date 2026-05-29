package org.Ch0p5h0p.mcjs.client.execution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class JSExecutor {
    public static String runScript(File file) throws IOException {
        ExecutionObj executor = new ExecutionObj(
                file.getName(),
                Files.readString(file.toPath())
        );
        try {
            return executor.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
