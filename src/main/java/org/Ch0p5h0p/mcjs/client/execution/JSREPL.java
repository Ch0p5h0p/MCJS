package org.Ch0p5h0p.mcjs.client.execution;

import io.github.stefanrichterhuber.quickjs.QuickJSContext;
import io.github.stefanrichterhuber.quickjs.QuickJSRuntime;
import org.Ch0p5h0p.mcjs.client.execution.libraries.LibCollector;

public class JSREPL {
    private QuickJSRuntime rt;
    private QuickJSContext ctx;

    public JSREPL() {
        this.rt = new QuickJSRuntime();
        this.ctx = rt.createContext();
        LibCollector.addLibraries(ctx);
    }

    public String runCode(String code) {
        try {

            Object result = ctx.eval(code);
            return result == null ? "undefined" : result.toString();
        } catch (Throwable t) {
            t.printStackTrace();

            return "ERROR EXECUTING SCRIPT: " + t.getClass().getName() + ": " + t.getMessage();
        }
    }

    public void close() throws Exception {
        if (ctx != null) {
            ctx.close();
            ctx = null;
        }
    }

}
