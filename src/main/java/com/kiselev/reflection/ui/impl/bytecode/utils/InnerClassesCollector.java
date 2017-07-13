package com.kiselev.reflection.ui.impl.bytecode.utils;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.exception.ByteCodeParserException;
import com.kiselev.reflection.ui.impl.exception.file.ReadFileException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by by Aleksei Makarov on 06/26/2017.
 */
public class InnerClassesCollector {

    public static Collection<Class<?>> getInnerClasses(Class<?> clazz) {
        Set<Class<?>> innerClasses = new HashSet<>();

        // Anonymous and synthetic classes
        innerClasses.addAll(getAnonymousOrSyntheticClasses(clazz));

        // Inner and nested classes
        innerClasses.addAll(getInnerAndNestedClasses(clazz));

        // Local classes
        innerClasses.addAll(getLocalClasses(clazz));

        return new ArrayList<>(innerClasses);
    }

    private static Collection<Class<?>> getAnonymousOrSyntheticClasses(Class<?> clazz) {
        Collection<Class<?>> anonymousOrSyntheticClasses = new ArrayList<>();

        int classId = 0;
        while (classId++ < 2 << 15) {
            try {
                Class<?> foundedClass = Class.forName(clazz.getName() + Constants.Symbols.DOLLAR + classId);
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

        if (ClassFileUtils.isDynamicCreateClass(clazz)) {
            ClassLoader classLoader = clazz.getClassLoader();
            Collection<Class<?>> classes = getLoadedClasses(classLoader);
            for (Class<?> loadedClass : classes) {
                String className = ClassNameUtils.getSimpleName(loadedClass);
                if (isLocalClass(clazz, className)) {
                    localClasses.add(loadedClass);
                }
            }
        } else {
            String path = ClassFileUtils.getFilePath(clazz);
            if (ClassFileUtils.isArchive(path)) {
                File file = new File(ClassFileUtils.getArchivePath(path));
                try {
                    JarFile jarFile = new JarFile(new URL(file.getPath()).getFile());
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.endsWith(Constants.Suffix.CLASS_FILE_SUFFIX)) {
                            name = name.substring(0, name.lastIndexOf(Constants.Symbols.DOT));
                            if (isLocalClass(clazz, name.substring(name.lastIndexOf(Constants.Symbols.SLASH, name.length()) + 1))) {
                                Class<?> localClass = Class.forName(name.replace(Constants.Symbols.SLASH, Constants.Symbols.DOT));
                                localClasses.add(localClass);
                            }
                        }
                    }
                } catch (IOException exception) {
                    throw new ReadFileException("Can't read jar file: " + file.getPath(), exception);
                } catch (ClassNotFoundException exception) {
                    throw new ByteCodeParserException("Can't load local class", exception);
                }
            } else {
                File file = new File(ClassFileUtils.getClassPackagePath(clazz));

                if (file.isDirectory()) {
                    File[] classes = file.listFiles();
                    if (classes != null) {
                        for (File classFile : classes) {
                            String fileName = classFile.getName();
                            if (fileName.endsWith(Constants.Suffix.CLASS_FILE_SUFFIX) && isLocalClass(clazz, fileName)) {
                                String className = ClassNameUtils.getPackageName(clazz) + Constants.Symbols.DOT
                                        + fileName.substring(0, fileName.lastIndexOf(Constants.Symbols.DOT));
                                try {
                                    Class<?> localClass = Class.forName(className);
                                    localClasses.add(localClass);
                                } catch (ClassNotFoundException exception) {
                                    throw new ByteCodeParserException("Can't load local class: " + fileName, exception);
                                }
                            }
                        }
                    }
                }
            }

        }

        return localClasses;
    }

    @SuppressWarnings("unchecked")
    private static Collection<Class<?>> getLoadedClasses(ClassLoader classLoader) {
        try {
            Field classes = ClassLoader.class.getDeclaredField("classes");
            classes.setAccessible(true);
            return (Collection<Class<?>>) classes.get(classLoader);
        } catch (ReflectiveOperationException exception) {
            throw new ByteCodeParserException("Can't get load classes", exception);
        }
    }

    private static boolean isLocalClass(Class<?> clazz, String className) {
        String name = ClassNameUtils.getSimpleName(clazz) + Constants.Symbols.DOLLAR + 1;
        return className.startsWith(name) && !className.equals(name);
    }
}
