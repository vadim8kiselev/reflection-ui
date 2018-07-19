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

    private JavaAgent agent;

    public JVMByteCodeCollector(JavaAgent javaAgent) {
        this.agent = javaAgent;
    }

    @Override
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public byte[] getByteCode(Class<?> clazz) {
        Instrumentation instrumentation = agent.getInstrumentation();
        ByteCodeHolder holder = agent.getByteCodeHolder();

        if (clazz != null) {
            synchronized (clazz) {
                try {
                    if (instrumentation != null) {
                        if (instrumentation.isRetransformClassesSupported() && instrumentation.isModifiableClass(clazz)) {
                            instrumentation.retransformClasses(clazz);
                        } else {
                            System.err.println("Class " + clazz.getName() + " is can't be retransform.");
                        }
                    } else {
                        System.err.println("Instrumentation instance is not initialize!");
                    }
                } catch (UnmodifiableClassException exception) {
                    String message = MessageFormat.format("Class: \"{0}\" is can't retransform", clazz.getName());
                    throw new InvalidRetransformClass(message, exception);
                }

                String javaBasedClassName = ClassNameUtils.getJavaBasedClassName(clazz);

                return holder.get(javaBasedClassName);
            }
        } else {
            return null;
        }
    }
}