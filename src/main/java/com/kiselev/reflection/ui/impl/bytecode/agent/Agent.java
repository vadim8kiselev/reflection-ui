package com.kiselev.reflection.ui.impl.bytecode.agent;

import com.kiselev.reflection.ui.exception.agent.InvalidRetransformClass;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassNameUtils;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
public final class Agent implements JavaAgent {

    private static Instrumentation instrumentation;

    private static Map<String, byte[]> byteCodeMap = new HashMap<>();

    public static void agentmain(String args, Instrumentation instrumentation) {
        Agent.instrumentation = instrumentation;
        instrumentation.addTransformer(new Transformer(), true);
    }

    static void uploadByteCode(String className, byte[] bytecode) {
        byteCodeMap.put(className, bytecode);
    }

    @Override
    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        try {
            if (instrumentation != null) {
                instrumentation.retransformClasses(clazz);
            }
        } catch (UnmodifiableClassException exception) {
            String message = String.format("Class: %s is can't retransform", clazz.getName());
            throw new InvalidRetransformClass(message, exception);
        }

        String javaBasedClassName = ClassNameUtils.getJavaBasedClassName(clazz);

        return byteCodeMap.get(javaBasedClassName);
    }
}
