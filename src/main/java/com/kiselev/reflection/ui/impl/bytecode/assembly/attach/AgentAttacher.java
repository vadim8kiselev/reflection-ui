package com.kiselev.reflection.ui.impl.bytecode.assembly.attach;

import com.ea.agentloader.AgentLoader;
import com.sun.tools.attach.VirtualMachine;

import java.lang.management.ManagementFactory;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public final class AgentAttacher {

    public static void attach(String agentPath) throws Exception {
        AgentLoader.loadAgent(agentPath, "");
    }
}
