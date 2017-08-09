package com.kiselev.reflection.ui.impl.bytecode.agent;

import java.lang.instrument.Instrumentation;

/**
 * Created by Aleksei Makarov on 08/03/2017.
 */
public interface JavaAgent {

    /**
     * Getting instrumentation from agent premain or agentmain method
     *
     * @return Instrumentation instance
     */
    Instrumentation getInstrumentation();

    /**
     * Getting bytecode holder which contains bytecode of retransform classes
     *
     * @return ByteCodeHolder instance
     */
    ByteCodeHolder getByteCodeHolder();
}
