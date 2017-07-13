package com.kiselev.reflection.ui.impl.bytecode.utils;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;

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

        // Temporary empty

        return localClasses;
    }
}
