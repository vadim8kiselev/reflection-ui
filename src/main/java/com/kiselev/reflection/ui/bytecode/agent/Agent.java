package com.kiselev.reflection.ui.bytecode.agent;

import com.kiselev.reflection.ui.bytecode.holder.ByteCodeHolder;

import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
public class Agent {

    public static void agentmain(String args, Instrumentation instrumentation) {
        try {
            instrumentation.addTransformer(new Transformer(), true);
            Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
            List<String> neededClasses = ByteCodeHolder.getClassesNamesForByteCode();

            for (Class loadedClass : allLoadedClasses) {
                if (neededClasses.contains(loadedClass.getName())) {
                    instrumentation.retransformClasses(loadedClass);
                }
            }

        } catch (Exception exception) {
            // sin
        }
    }
}
