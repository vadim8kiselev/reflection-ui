package com.classparser.bytecode.impl.assembly.attach;

import com.ea.agentloader.AgentLoader;

public final class AgentAttacher {

    private static final String EMPTY_OPTIONS = "";

    public static void attach(String agentPath) {
        AgentLoader.loadAgent(agentPath, EMPTY_OPTIONS);
    }
}
