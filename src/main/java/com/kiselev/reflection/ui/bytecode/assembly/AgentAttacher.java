package com.kiselev.reflection.ui.bytecode.assembly;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public class AgentAttacher {

    static void attach(String agentPath) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        String virtualMachineProcessId = getVirtualMachineProcessId();
        VirtualMachine virtualMachine = VirtualMachine.attach(virtualMachineProcessId);
        virtualMachine.loadAgent(agentPath, "");
        virtualMachine.detach();
    }

    private static String getVirtualMachineProcessId() {
        String virtualMachineName = ManagementFactory.getRuntimeMXBean().getName();
        return virtualMachineName.substring(0, virtualMachineName.indexOf('@'));
    }
}
