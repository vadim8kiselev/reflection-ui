package com.kiselev.classparser.impl.bytecode.collector;

import com.kiselev.classparser.api.bytecode.agent.ByteCodeHolder;
import com.kiselev.classparser.api.bytecode.agent.JavaAgent;
import com.kiselev.classparser.api.bytecode.collector.ByteCodeCollector;
import com.kiselev.classparser.exception.agent.InvalidRetransformClass;
import com.kiselev.classparser.impl.bytecode.configuration.StateManager;
import com.kiselev.classparser.impl.bytecode.utils.ClassNameUtils;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class FromJVMByteCodeCollector implements ByteCodeCollector {

    private static JavaAgent agent = StateManager.getConfiguration().getAgent();

    private Instrumentation instrumentation = agent.getInstrumentation();

    private ByteCodeHolder holder = agent.getByteCodeHolder();

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        if (clazz != null) {
            try {
                if (instrumentation != null && instrumentation.isModifiableClass(clazz)) {
                    instrumentation.retransformClasses(clazz);
                }
            } catch (UnmodifiableClassException exception) {
                String message = String.format("Class: %s is can't retransform", clazz.getName());
                throw new InvalidRetransformClass(message, exception);
            }

            String javaBasedClassName = ClassNameUtils.getJavaBasedClassName(clazz);

            return holder.get(javaBasedClassName);
        } else {
            return null;
        }
    }
}
