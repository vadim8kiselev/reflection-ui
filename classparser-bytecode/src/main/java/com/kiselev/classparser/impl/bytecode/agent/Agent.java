package com.kiselev.classparser.impl.bytecode.agent;

import com.kiselev.classparser.impl.bytecode.assembly.AgentAssembler;

import java.lang.instrument.Instrumentation;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
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
}
