package com.classparser.bytecode.impl.utils;

import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.bytecode.impl.configuration.ConfigurationManager;
import com.classparser.exception.agent.ClassLoadException;
import com.classparser.exception.file.ReadFileException;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class InnerClassesCollector {

    private static final String LOCAL_CLASS_PATTERN = "[\\d]+.*";

    private static final String POSITIVE_NUMBER_PATTERN = "\\d+";

    private final ConfigurationManager configurationManager;

    public InnerClassesCollector(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public List<Class<?>> getInnerClasses(Class<?> clazz) {
        Set<Class<?>> innerClasses = new HashSet<>();

        if (configurationManager.isDecompileAnonymousClasses()) {
            innerClasses.addAll(getAnonymousOrSyntheticClasses(clazz));
        }

        if (configurationManager.isDecompileInnerAndNestedClasses()) {
            innerClasses.addAll(getInnerAndNestedClasses(clazz));
        }

        if (configurationManager.isDecompileLocalClasses()) {
            innerClasses.addAll(getLocalClasses(clazz));
        }

        return new ArrayList<>(innerClasses);
    }

    private List<Class<?>> getAnonymousOrSyntheticClasses(Class<?> clazz) {
        List<Class<?>> anonymousOrSyntheticClasses = new ArrayList<>();

        int classId = 0;
        while (classId++ < 2 << 15) {
            try {
                Class<?> foundedClass = Class.forName(clazz.getName() + '$' + classId);
                anonymousOrSyntheticClasses.add(foundedClass);
                anonymousOrSyntheticClasses.addAll(getInnerClasses(foundedClass));
            } catch (Exception exception) {
                break;
            }
        }

        return anonymousOrSyntheticClasses;
    }

    private List<Class<?>> getInnerAndNestedClasses(Class<?> clazz) {
        List<Class<?>> innerAndNestedClasses = new ArrayList<>();

        for (Class<?> innerOrNestedClass : clazz.getDeclaredClasses()) {
            innerAndNestedClasses.add(innerOrNestedClass);
            innerAndNestedClasses.addAll(getInnerClasses(innerOrNestedClass));
        }

        return innerAndNestedClasses;
    }

    private List<Class<?>> getLocalClasses(Class<?> clazz) {
        List<Class<?>> localClasses = new ArrayList<>();

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

    private boolean isDynamicCreateClass(Class<?> clazz) {
        return ClassFileUtils.getFilePath(clazz).isEmpty();
    }

    private List<Class<?>> collectLocalDynamicClass(Class<?> clazz) {
        List<Class<?>> localClasses = new ArrayList<>();

        ClassLoader loader = ClassLoadUtils.getClassLoader(clazz);

        List<Class<?>> classes = ClassLoadUtils.getLoadedClasses(loader, configurationManager.getAgent());
        for (Class<?> loadedClass : classes) {
            String className = ClassNameUtils.getSimpleName(loadedClass);
            if (loadedClass.isLocalClass() && isLocalClass(clazz, className)) {
                addLocalClass(loadedClass, localClasses);
            }
        }

        return localClasses;
    }

    private List<Class<?>> collectLocalClassesFromArchive(Class<?> clazz) {
        List<Class<?>> localClasses = new ArrayList<>();

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
            String message = MessageFormat.format("Can't read jar file: {0}", file.getPath());
            throw new ReadFileException(message, exception);
        }

        return localClasses;
    }

    private List<Class<?>> collectLocalClassesFromDirectory(Class<?> clazz) {
        List<Class<?>> localClasses = new ArrayList<>();

        File file = new File(ClassFileUtils.getClassPackagePath(clazz));
        File[] classes = file.listFiles();

        if (classes != null) {
            for (File classFile : classes) {
                String name = ClassNameUtils.getPackageName(clazz) + '.' + classFile.getName();
                addLocalClass(collectLocalStaticClass(clazz, name), localClasses);
            }
        }

        return localClasses;
    }

    private void addLocalClass(Class<?> localClass, Collection<Class<?>> localClasses) {
        if (localClass != null) {
            localClasses.add(localClass);
            localClasses.addAll(getInnerClasses(localClass));
        }
    }

    private Class<?> collectLocalStaticClass(Class<?> clazz, String name) {
        String fullName = ClassNameUtils.normalizeFullName(name);
        String simpleName = ClassNameUtils.normalizeSimpleName(name);

        if (name.endsWith(Constants.Suffix.CLASS_FILE_SUFFIX) && isLocalClass(clazz, simpleName)) {
            try {
                return Class.forName(fullName);
            } catch (ClassNotFoundException exception) {
                String message = MessageFormat.format("Can't load local class: {0}", fullName);
                throw new ClassLoadException(message, exception);
            }
        }

        return null;
    }

    private boolean isLocalClass(Class<?> clazz, String className) {
        String name = ClassNameUtils.getSimpleName(clazz) + '$';

        return getPattern(name).matcher(className).matches() &&
                !isNumber(ClassStringUtils.delete(className, name)) &&
                !ClassStringUtils.delete(className, name).contains("$");
    }

    private boolean isNumber(String line) {
        return Pattern.compile(POSITIVE_NUMBER_PATTERN).matcher(line).matches();
    }

    private Pattern getPattern(String name) {
        String pattern = name.replace("$", File.separator + '$');
        return Pattern.compile(pattern + LOCAL_CLASS_PATTERN);
    }
}