package com.kiselev.classparser.impl.bytecode.assembly.attach;

import com.ea.agentloader.AgentLoader;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public final class AgentAttacher {

    private static final String EMPTY_OPTIONS = "";

    public static void attach(String agentPath) {
        AgentLoader.loadAgent(agentPath, EMPTY_OPTIONS);
    }

    public static void attach(String agentPath, String options) {
        AgentLoader.loadAgent(agentPath, options);
    }
}
