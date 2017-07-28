package com.kiselev.reflection.ui.impl.bytecode.assembly.attach;

import com.ea.agentloader.AgentLoader;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public final class AgentAttacher {

    private static final String EMPTY_OPTIONS = "";

    public static void attach(String agentPath) throws Exception {
        AgentLoader.loadAgent(agentPath, EMPTY_OPTIONS);
    }

    public static void attach(String agentPath, String options) throws Exception {
        AgentLoader.loadAgent(agentPath, options);
    }
}
