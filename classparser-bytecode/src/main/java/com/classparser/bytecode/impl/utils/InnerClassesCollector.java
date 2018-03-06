package com.classparser.bytecode.impl.utils;

import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.bytecode.impl.configuration.StateManager;
import com.classparser.exception.agent.ClassLoadException;
import com.classparser.exception.file.ReadFileException;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class InnerClassesCollector {

    private static final String LOCAL_CLASS_PATTERN = "[\\d]+.*";

    private static final String POSITIVE_NUMBER_PATTERN = "\\d+";

    private static final JavaAgent agent = StateManager.getConfiguration().getAgent();

    public static Collection<Class<?>> getInnerClasses(Class<?> clazz) {
        Set<Class<?>> innerClasses = new HashSet<>();

        // Anonymous and synthetic classes
        if (StateManager.getConfiguration().isDecompileAnonymousClasses()) {
            innerClasses.addAll(getAnonymousOrSyntheticClasses(clazz));
        }

        // Inner and nested classes
        if (StateManager.getConfiguration().isDecompileInnerAndNestedClasses()) {
            innerClasses.addAll(getInnerAndNestedClasses(clazz));
        }

        // Local classes
        if (StateManager.getConfiguration().isDecompileLocalClasses()) {
            innerClasses.addAll(getLocalClasses(clazz));
        }

        return new ArrayList<>(innerClasses);
    }

    private static Collection<Class<?>> getAnonymousOrSyntheticClasses(Class<?> clazz) {
        Collection<Class<?>> anonymousOrSyntheticClasses = new ArrayList<>();

        int classId = 0;
        while (classId++ < 2 << 15) {
            try {
                Class<?> foundedClass = Class.forName(clazz.getName() + "$" + classId);
                anonymousOrSyntheticClasses.add(foundedClass);
                anonymousOrSyntheticClasses.addAll(getInnerClasses(foundedClass));
            } catch (Exception exception) {
                break;
            }
        }

        return anonymousOrSyntheticClasses;
    }

    private static Collection<Class<?>> getInnerAndNestedClasses(Class<?> clazz) {
        Collection<Class<?>> innerAndNestedClasses = new ArrayList<>();

        for (Class<?> innerOrNestedClass : clazz.getDeclaredClasses()) {
            innerAndNestedClasses.add(innerOrNestedClass);
            innerAndNestedClasses.addAll(getInnerClasses(innerOrNestedClass));
        }

        return innerAndNestedClasses;
    }

    private static Collection<Class<?>> getLocalClasses(Class<?> clazz) {
        Collection<Class<?>> localClasses = new ArrayList<>();

        if (isDynamicCreateClass(clazz)) {
            localClasses.addAll(collectLocalDynamicClass(clazz));
        } else {
            if (ClassFileUtils.isArchive(ClassFileUtils.getFilePath(clazz))) {
                localClasses.addAll(collectLocalClassesFromArchive(clazz));
            } else {
                localClasses.addAll(collectLocalClassesFromDirectory(clazz));
            }
        }

        return localClasses;
    }

    private static boolean isDynamicCreateClass(Class<?> clazz) {
        return ClassFileUtils.getFilePath(clazz).isEmpty();
    }

    private static Collection<Class<?>> collectLocalDynamicClass(Class<?> clazz) {
        Collection<Class<?>> localClasses = new ArrayList<>();

        ClassLoader loader = ClassFileUtils.getClassLoader(clazz);

        Collection<Class<?>> classes = getLoadedClasses(loader);
        for (Class<?> loadedClass : classes) {
            String className = ClassNameUtils.getSimpleName(loadedClass);
            if (loadedClass.isLocalClass() && isLocalClass(clazz, className)) {
                addLocalClass(loadedClass, localClasses);
            }
        }

        return localClasses;
    }

    private static Collection<Class<?>> collectLocalClassesFromArchive(Class<?> clazz) {
        Collection<Class<?>> localClasses = new ArrayList<>();

        String path = ClassFileUtils.getFilePath(clazz);
        File file = new File(ClassFileUtils.getArchivePath(path));
        try {
            JarFile jarFile = new JarFile(file.getPath());
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                addLocalClass(collectLocalStaticClass(clazz, entry.getName()), localClasses);
            }
        } catch (IOException exception) {
            throw new ReadFileException(String.format("Can't read jar file: %s", file.getPath()), exception);
        }

        return localClasses;
    }

    private static Collection<Class<?>> collectLocalClassesFromDirectory(Class<?> clazz) {
        Collection<Class<?>> localClasses = new ArrayList<>();

        File file = new File(ClassFileUtils.getClassPackagePath(clazz));
        File[] classes = file.listFiles();

        if (classes != null) {
            for (File classFile : classes) {
                String name = ClassNameUtils.getPackageName(clazz) + "." + classFile.getName();
                addLocalClass(collectLocalStaticClass(clazz, name), localClasses);
            }
        }

        return localClasses;
    }

    private static void addLocalClass(Class<?> localClass, Collection<Class<?>> localClasses) {
        if (localClass != null) {
            localClasses.add(localClass);
            localClasses.addAll(getInnerClasses(localClass));
        }
    }

    private static Class<?> collectLocalStaticClass(Class<?> clazz, String name) {
        String fullName = ClassNameUtils.normalizeFullName(name);
        String simpleName = ClassNameUtils.normalizeSimpleName(name);

        if (name.endsWith(Constants.Suffix.CLASS_FILE_SUFFIX) && isLocalClass(clazz, simpleName)) {
            try {
                return Class.forName(fullName);
            } catch (ClassNotFoundException exception) {
                throw new ClassLoadException(String.format("Can't load local class: %s", fullName), exception);
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static Collection<Class<?>> getLoadedClasses(ClassLoader classLoader) {
        if (!agent.isInitialize()) {
            try {
                Field classes = ClassLoader.class.getDeclaredField("classes");
                classes.setAccessible(true);
                return (Collection<Class<?>>) classes.get(classLoader);
            } catch (ReflectiveOperationException exception) {
                throw new ClassLoadException("Can't get loaded classes", exception);
            }
        } else {
            Instrumentation instrumentation = agent.getInstrumentation();
            return Arrays.asList(instrumentation.getInitiatedClasses(classLoader));
        }
    }

    private static boolean isLocalClass(Class<?> clazz, String className) {
        String name = ClassNameUtils.getSimpleName(clazz) + "$";

        return getPattern(name).matcher(className).matches() &&
                !isNumber(ClassStringUtils.delete(className, name)) &&
                !ClassStringUtils.delete(className, name).contains("$");
    }

    private static boolean isNumber(String line) {
        return Pattern.compile(POSITIVE_NUMBER_PATTERN).matcher(line).matches();
    }

    private static Pattern getPattern(String name) {
        String pattern = name.replace("$", File.separator + "$");
        return Pattern.compile(pattern + LOCAL_CLASS_PATTERN);
    }
}
