package com.kiselev.reflection.ui.bytecode.agent;

import com.kiselev.reflection.ui.bytecode.holder.ByteCodeHolder;

import java.lang.instrument.Instrumentation;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
public class Agent {

    public static void agentmain(String args, Instrumentation instrumentation) {
        try {
            instrumentation.addTransformer(new Transformer(), true);
            ByteCodeHolder.registerInstrumentation(instrumentation);
        } catch (Exception exception) {
            // sin
        }
    }
}
