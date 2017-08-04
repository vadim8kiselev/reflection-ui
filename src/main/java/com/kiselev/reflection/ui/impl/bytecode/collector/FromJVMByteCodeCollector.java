package com.kiselev.reflection.ui.impl.bytecode.collector;

import com.kiselev.reflection.ui.exception.agent.InvalidRetransformClass;
import com.kiselev.reflection.ui.impl.bytecode.agent.JavaAgent;
import com.kiselev.reflection.ui.impl.bytecode.configuration.StateManager;
import com.kiselev.reflection.ui.impl.bytecode.holder.ByteCodeHolder;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassNameUtils;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class FromJVMByteCodeCollector implements ByteCodeCollector {

    private static JavaAgent agent = StateManager.getConfiguration().getAgent();

    private Instrumentation instrumentation = agent.getInstrumentation();

    private ByteCodeHolder holder = agent.getByteCodeHolder();

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

        return holder.get(javaBasedClassName);
    }
}
