package com.classparser.bytecode.impl.agent;

import com.classparser.bytecode.api.agent.ByteCodeHolder;
import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.impl.assembly.AgentAssembler;

import java.lang.instrument.Instrumentation;

public final class Agent implements JavaAgent {

    private static final RetransformClassStorage RETRANSFORM_CLASS_STORAGE = new RetransformClassStorage();

    private static Instrumentation instrumentation;

    private AgentAssembler agentAssembler;

    private static boolean isInitialize = false;

    public Agent(AgentAssembler agentAssembler) {
        this.agentAssembler = agentAssembler;
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        Agent.instrumentation = instrumentation;
        instrumentation.addTransformer(RETRANSFORM_CLASS_STORAGE, true);
    }

    @Override
    public Instrumentation getInstrumentation() {
        if (!isInitialize()) {
            initialize();
        }

        return instrumentation;
    }

    private synchronized void initialize() {
        if (!isInitialize()) {
            this.agentAssembler.assembly(this);
            isInitialize = true;
        }
    }

    @Override
    public ByteCodeHolder getByteCodeHolder() {
        if (!isInitialize()) {
            initialize();
        }

        return RETRANSFORM_CLASS_STORAGE;
    }

    @Override
    public boolean isInitialize() {
        return isInitialize;
    }
}