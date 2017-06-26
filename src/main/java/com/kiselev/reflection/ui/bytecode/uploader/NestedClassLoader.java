package com.kiselev.reflection.ui.bytecode.uploader;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by alma0317 on 26.06.2017.
 */
public class NestedClassLoader {

    private Class<?> currentClass;

    private Set<Class<?>> nestedClasses;

    public NestedClassLoader(Class<?> clazz) {
        this.currentClass = clazz;
        this.nestedClasses = new HashSet<>();
    }

    public List<Class<?>> getNestedClasses() {
        nestedClasses.addAll(getAnonymousClasses(currentClass));
        List<Class<?>> memberClasses = Arrays.asList(currentClass.getDeclaredClasses());
        for (Class<?> memberClass : memberClasses) {
            nestedClasses.add(memberClass);
            currentClass = memberClass;
            nestedClasses.addAll(getNestedClasses());
        }

        if (currentClass.getEnclosingClass() != null) {
            this.currentClass = currentClass.getEnclosingClass();
        }

        return new ArrayList<>(nestedClasses);
    }

    private List<Class<?>> getAnonymousClasses(Class<?> clazz) {
        List<Class<?>> anonymousClasses = new ArrayList<>();
        String className = clazz.getName();
        Class<?> currentClass = this.currentClass;
        int currentNumber = 1;
        try {
            while (true) {
                Class<?> memberClass = Class.forName(className + "$" + currentNumber);
                anonymousClasses.add(memberClass);
                this.currentClass = memberClass;
                anonymousClasses.addAll(getNestedClasses());
                currentNumber++;
            }
        } catch (ClassNotFoundException exception) {
            //nothing
        }

        this.currentClass = currentClass;

        return anonymousClasses;
    }

    /**
     * Worked in case, how local class is loaded in jvm
     * This operation is very "hard" and unsafe
     * */
    private List<Class<?>> tryGetLocalClasses(Class<?> clazz) {
        List<Class<?>> localClasses = new ArrayList<>();

        for (Class<?> loadedClass : getLoadedClasses(clazz)) {
            if (loadedClass.getName().contains(clazz.getName())) {
                localClasses.add(loadedClass);
            }
        }

        return localClasses;
    }

    @SuppressWarnings("unchecked")
    private List<Class<?>> getLoadedClasses(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = clazz.getClassLoader();

            Field allLoadedClasses = ClassLoader.class.getDeclaredField("classes");
            allLoadedClasses.setAccessible(true);
            Vector<Class<?>> loadedClasses = (Vector<Class<?>>) allLoadedClasses.get(classLoader);
            classes.addAll(loadedClasses);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }

        return classes;
    }
}
