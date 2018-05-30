package com.classparser.bytecode.impl.collector;

import com.classparser.bytecode.api.agent.ByteCodeHolder;
import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.exception.agent.InvalidRetransformClass;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.text.MessageFormat;

public class JVMByteCodeCollector implements ByteCodeCollector {

    private final JavaAgent agent;

    private Instrumentation instrumentation;

    private ByteCodeHolder holder;

    public JVMByteCodeCollector(JavaAgent agent) {
        this.agent = agent;
    }

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        initAgent();
        if (clazz != null) {
            try {
                if (instrumentation != null && instrumentation.isModifiableClass(clazz)) {
                    instrumentation.retransformClasses(clazz);
                }
            } catch (UnmodifiableClassException exception) {
                String message = MessageFormat.format("Class: \"{0}\" is can't retransform", clazz.getName());
                throw new InvalidRetransformClass(message, exception);
            }

            String javaBasedClassName = ClassNameUtils.getJavaBasedClassName(clazz);

            return holder.get(javaBasedClassName);
        } else {
            return null;
        }
    }

    private void initAgent() {
        if (instrumentation == null || holder == null) {
            instrumentation = agent.getInstrumentation();
            holder = agent.getByteCodeHolder();
        }
    }
}