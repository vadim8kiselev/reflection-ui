package com.classparser.bytecode.impl.agent;

import com.classparser.bytecode.api.agent.ByteCodeHolder;
import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.impl.assembly.AgentAssembler;
import com.classparser.bytecode.impl.configuration.ConfigurationManager;

import java.lang.instrument.Instrumentation;

public final class Agent implements JavaAgent {

    private static final RetransformClassStorage CLASS_STORAGE = new RetransformClassStorage();

    private static Instrumentation instrumentation;

    private static volatile boolean isInitialize = false;

    private final AgentAssembler agentAssembler;

    private volatile ConfigurationManager configurationManager;

    public Agent(AgentAssembler agentAssembler) {
        this.agentAssembler = agentAssembler;
        this.agentAssembler.setConfigurationManager(configurationManager);
    }

    public static void premain(String args, Instrumentation instrument) {
        instrumentation = instrument;
        instrumentation.addTransformer(CLASS_STORAGE, true);
        isInitialize = true;
    }

    public static void agentmain(String args, Instrumentation instrument) {
        instrumentation = instrument;
        instrumentation.addTransformer(CLASS_STORAGE, true);
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

        return CLASS_STORAGE;
    }

    @Override
    public boolean isInitialize() {
        return isInitialize;
    }

    @Override
    public void setConfiguration(ConfigurationManager configuration) {
        if (this.configurationManager == null) {
            this.configurationManager = configuration;
        }
    }
}