package com.classparser.bytecode.impl.collector;

import com.classparser.bytecode.api.agent.ByteCodeHolder;
import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.impl.configuration.StateManager;
import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.exception.agent.InvalidRetransformClass;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.text.MessageFormat;

public class FromJVMByteCodeCollector implements ByteCodeCollector {

    private static final JavaAgent AGENT = StateManager.getConfiguration().getAgent();

    private Instrumentation instrumentation;

    private ByteCodeHolder holder;

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        if (instrumentation == null || holder == null) {
            instrumentation = AGENT.getInstrumentation();
            holder = AGENT.getByteCodeHolder();
        }

        if (clazz != null) {
            try {
                if (instrumentation != null && instrumentation.isModifiableClass(clazz)) {
                    instrumentation.retransformClasses(clazz);
                }
            } catch (UnmodifiableClassException exception) {
                String message = MessageFormat.format("Class: {} is can't retransform", clazz.getName());
                throw new InvalidRetransformClass(message, exception);
            }

            String javaBasedClassName = ClassNameUtils.getJavaBasedClassName(clazz);

            return holder.get(javaBasedClassName);
        } else {
            return null;
        }
    }
}
