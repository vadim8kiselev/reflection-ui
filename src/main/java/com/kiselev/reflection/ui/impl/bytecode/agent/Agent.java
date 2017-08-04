package com.kiselev.reflection.ui.impl.bytecode.agent;

import com.kiselev.reflection.ui.impl.bytecode.assembly.AgentAssembler;
import com.kiselev.reflection.ui.impl.bytecode.holder.ByteCodeHolder;

import java.lang.instrument.Instrumentation;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
public final class Agent implements JavaAgent {

    private static Instrumentation instrumentation;

    private static final AgentAssembler agentAssembler = new AgentAssembler();

    private static ByteCodeHolder holder;

    public Agent(ByteCodeHolder holder) {
        Agent.holder = holder;
        initialize();
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        Agent.instrumentation = instrumentation;
        instrumentation.addTransformer(new Transformer(holder), true);
    }

    @Override
    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    private void initialize() {
        if (!agentAssembler.isAssembled()) {
            agentAssembler.assembly();
        }
    }

    @Override
    public ByteCodeHolder getByteCodeHolder() {
        return holder;
    }
}
