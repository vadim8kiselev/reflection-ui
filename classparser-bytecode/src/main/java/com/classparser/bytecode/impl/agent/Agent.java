package com.classparser.bytecode.impl.agent;

import com.classparser.bytecode.api.agent.ByteCodeHolder;
import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.impl.assembly.AgentAssembler;

import java.lang.instrument.Instrumentation;

public final class Agent implements JavaAgent {

    private static final AgentAssembler AGENT_ASSEMBLER = new AgentAssembler();

    private static final Transformer TRANSFORMER = new Transformer();

    private static Instrumentation instrumentation;

    public static void agentmain(String args, Instrumentation instrumentation) {
        Agent.instrumentation = instrumentation;
        instrumentation.addTransformer(TRANSFORMER, true);
    }

    @Override
    public Instrumentation getInstrumentation() {
        initialize();
        return instrumentation;
    }

    private void initialize() {
        if (!isInitialize()) {
            AGENT_ASSEMBLER.assembly();
        }
    }

    @Override
    public ByteCodeHolder getByteCodeHolder() {
        initialize();
        return TRANSFORMER;
    }

    @Override
    public boolean isInitialize() {
        return AGENT_ASSEMBLER.isAssembled();
    }
}
