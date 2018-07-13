package com.classparser.bytecode.impl.utils;

import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.exception.agent.ClassLoadException;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ClassLoadUtils {

    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader loader = null;

        if (clazz != null) {
            loader = clazz.getClassLoader();

            if (loader == null) {
                loader = ClassLoader.getSystemClassLoader();
                while (loader != null && loader.getParent() != null) {
                    loader = loader.getParent();
                }
            }
        }

        return loader;
    }

    @SuppressWarnings("unchecked")
    public static List<Class<?>> getLoadedClasses(ClassLoader classLoader, JavaAgent agent) {
        if (agent != null && agent.isInitialize()) {
            Instrumentation instrumentation = agent.getInstrumentation();
            return Arrays.asList(instrumentation.getInitiatedClasses(classLoader));
        } else {
            try {
                Field classesField = ClassLoader.class.getDeclaredField("classes");
                classesField.setAccessible(true);
                Collection<Class<?>> loadedClasses = (Collection<Class<?>>) classesField.get(classLoader);
                classesField.setAccessible(false);
                return new ArrayList<>(loadedClasses);
            } catch (ReflectiveOperationException exception) {
                throw new ClassLoadException("Can't get loaded classes", exception);
            }
        }
    }

    public static List<Class<?>> getLoadedClasses(ClassLoader classLoader) {
        return getLoadedClasses(classLoader, null);
    }
}
