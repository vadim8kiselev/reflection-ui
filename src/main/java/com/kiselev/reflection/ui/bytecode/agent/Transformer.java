package com.kiselev.reflection.ui.bytecode.agent;

import com.kiselev.reflection.ui.bytecode.holder.ByteCodeHolder;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
public class Transformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] byteCode) throws IllegalClassFormatException {

        Class<?> clazz = getClassByName(transformClassName(className));
        if (clazz != null) {
            ByteCodeHolder.uploadByteCodeForClass(clazz, byteCode);
        }
        return null;
    }

    private String transformClassName(String canonicalClassName) {
        return canonicalClassName.replace("/", ".");
    }

    private Class<?> getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (Exception exception) {
            return null;
        }
    }
}
