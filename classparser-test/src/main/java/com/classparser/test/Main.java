package com.classparser.test;

import com.classparser.api.ClassParser;
import com.classparser.reflection.impl.ReflectionParser;

public class Main {

    public static void main(String[] args) {
        ClassParser parser = new ReflectionParser();
        System.out.println(parser.parseClass(Class.class));
    }
}
