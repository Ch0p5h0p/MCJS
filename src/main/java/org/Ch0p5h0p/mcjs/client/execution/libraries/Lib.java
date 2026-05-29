package org.Ch0p5h0p.mcjs.client.execution.libraries;

import io.github.stefanrichterhuber.quickjs.QuickJSContext;

public interface Lib {
    // Function to add library things to a context
    void init(QuickJSContext ctx);

    // Documentation functions
    String name();          // returns library name
    String docs();          // returns library documentation
}
