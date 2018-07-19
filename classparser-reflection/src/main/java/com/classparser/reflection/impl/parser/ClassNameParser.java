package com.classparser.reflection.impl.parser;

import com.classparser.reflection.impl.parser.imports.ImportParser;
import com.classparser.reflection.impl.configuration.ReflectionParserManager;

import java.lang.reflect.Member;

public class ClassNameParser {

    private final ImportParser importParser;

    private final ReflectionParserManager manager;

    public ClassNameParser(ImportParser importParser, ReflectionParserManager manager) {
        this.importParser = importParser;
        this.manager = manager;
    }

    public String getTypeName(Class<?> clazz) {
        return importParser.addImport(clazz) ? getSimpleName(clazz) : getName(clazz);
    }

    public String getSimpleName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        if (typeName.isEmpty()) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
        }
        return typeName;
    }

    public String getName(Class<?> clazz) {
        if (clazz.isMemberClass() || clazz == manager.getParsedClass()) {
            return getSimpleName(clazz);
        } else {
            return clazz.getName();
        }
    }

    public String getMemberName(Member member) {
        return member.getName();
    }
}