package com.kiselev.reflection.ui.configuration.reflection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 07/14/2017.
 */
public class ReflectionParserConfiguration implements ReflectionConfiguration {

    private HashMap<String, Object> configuration = new HashMap<>();

    public ReflectionParserConfiguration() {

    }

    @Override
    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    @Override
    public ReflectionConfiguration showAnnotationTypes(boolean flag) {
        return null;
    }

    @Override
    public ReflectionConfiguration showInnerClasses(boolean flag) {
        return null;
    }

    @Override
    public ReflectionConfiguration showNonJavaModifiers(boolean flag) {
        return null;
    }

    @Override
    public ReflectionConfiguration showDefaultValueInAnnotation(boolean flag) {
        return null;
    }

    @Override
    public ReflectionConfiguration setCountIndentSpaces(int count) {
        return null;
    }

    @Override
    public ReflectionConfiguration defineNewLineCharacter(String character) {
        return null;
    }
}
