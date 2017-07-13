package com.kiselev.reflection.ui.impl.bytecode.agent;

import com.kiselev.reflection.ui.impl.bytecode.holder.ByteCodeHolder;

import java.lang.instrument.Instrumentation;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
public final class Agent {

    public static void agentmain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new Transformer(), true);
        ByteCodeHolder.registerInstrumentation(instrumentation);
    }
}
