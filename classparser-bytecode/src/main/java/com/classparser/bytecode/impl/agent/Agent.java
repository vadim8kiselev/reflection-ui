package com.classparser.bytecode.impl.agent;

import com.classparser.bytecode.api.agent.ByteCodeHolder;
import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.impl.assembly.AgentAssembler;

import java.lang.instrument.Instrumentation;

public final class Agent implements JavaAgent {

    private static final AgentAssembler agentAssembler = new AgentAssembler();

    private static Instrumentation instrumentation;

    private static Transformer transformer = new Transformer();

    public static void agentmain(String args, Instrumentation instrumentation) {
        Agent.instrumentation = instrumentation;
        instrumentation.addTransformer(transformer, true);
    }

    @Override
    public Instrumentation getInstrumentation() {
        initialize();
        return instrumentation;
    }

    private void initialize() {
        if (!agentAssembler.isAssembled()) {
            agentAssembler.assembly();
        }
    }

    @Override
    public ByteCodeHolder getByteCodeHolder() {
        initialize();
        return transformer;
    }

    @Override
    public boolean isInitialize() {
        return agentAssembler.isAssembled();
    }
}