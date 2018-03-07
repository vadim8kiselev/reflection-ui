package com.classparser.bytecode.impl.assembly.attach;

import com.ea.agentloader.AgentLoader;

public final class AgentAttacher {

    public static void attach(String agentPath) {
        AgentLoader.loadAgent(agentPath, "");
    }
}
