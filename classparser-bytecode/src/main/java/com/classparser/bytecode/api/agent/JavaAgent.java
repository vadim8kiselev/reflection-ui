package com.classparser.bytecode.api.agent;

import java.lang.instrument.Instrumentation;

public interface JavaAgent {

    /**
     * Getting instrumentation from agent premain or agentmain method
     * Execute initialize if agent is not initialized
     *
     * @return {@link Instrumentation} instance
     */
    Instrumentation getInstrumentation();

    /**
     * Getting bytecode holder which contains bytecode of retransform classes
     * Execute initialize if agent is not initialized
     *
     * @return {@link ByteCodeHolder} instance
     */
    ByteCodeHolder getByteCodeHolder();

    /**
     * Try check agent is initialize
     *
     * @return boolean status
     */
    boolean isInitialize();
}