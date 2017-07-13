package com.kiselev.reflection.ui.impl.bytecode.holder;

import com.kiselev.reflection.ui.impl.bytecode.assembly.AgentAssembler;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassNameResolver;
import com.kiselev.reflection.ui.impl.exception.agent.InvalidRetransformClass;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public class ByteCodeHolder {

    private static Map<String, byte[]> byteCodeMap = new HashMap<>();

    private static AgentAssembler assembler = new AgentAssembler(); // TODO : point of extension

    private static Instrumentation instrumentation;

    public static void uploadByteCodeForClass(String className, byte[] byteCode) {
        byteCodeMap.put(className, byteCode);
    }

    public static byte[] getByteCode(Class<?> clazz) {
        if (!assembler.isAssembled()) {
            assembler.assembly();
        }

        retransformClass(clazz);

        String javaBasedClassName = ClassNameResolver.resolveJavaBasedClassName(clazz);

        return byteCodeMap.get(javaBasedClassName);
    }

    public static void registerInstrumentation(Instrumentation instrumentation) {
        ByteCodeHolder.instrumentation = instrumentation;
    }

    private static void retransformClass(Class<?> clazz) {
        try {
            if (instrumentation != null) {
                instrumentation.retransformClasses(clazz);
            }
        } catch (UnmodifiableClassException exception) {
            throw new InvalidRetransformClass("Class: " + clazz.getName() + " is can't retransform", exception);
        }
    }
}
