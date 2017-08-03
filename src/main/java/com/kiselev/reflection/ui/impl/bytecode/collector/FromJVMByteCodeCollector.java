package com.kiselev.reflection.ui.impl.bytecode.collector;

import com.kiselev.reflection.ui.impl.bytecode.agent.Agent;
import com.kiselev.reflection.ui.impl.bytecode.agent.JavaAgent;
import com.kiselev.reflection.ui.impl.bytecode.assembly.AgentAssembler;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class FromJVMByteCodeCollector implements ByteCodeCollector {

    private static AgentAssembler assembler = new AgentAssembler();

    private static JavaAgent agent;

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        assembled();
        return agent.getByteCode(clazz);
    }

    private void assembled() {
        if (!assembler.isAssembled()) {
            assembler.assembly();
            agent = new Agent();
        }
    }
}
