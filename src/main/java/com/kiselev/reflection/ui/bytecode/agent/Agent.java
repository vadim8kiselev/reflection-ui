package com.kiselev.reflection.ui.bytecode.agent;

import java.lang.instrument.Instrumentation;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
public class Agent {

    public static void agentmain(String args, Instrumentation instrumentation) {
        try {
            instrumentation.addTransformer(new Transformer(), true);
            Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
            for (Class loadedClass : allLoadedClasses) {
                if (!loadedClass.isArray()) {
                    instrumentation.retransformClasses(loadedClass);
                }
            }

        } catch (Exception exception) {
            // sin
        }
    }
}
