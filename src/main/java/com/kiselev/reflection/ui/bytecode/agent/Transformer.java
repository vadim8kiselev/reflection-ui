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

        ByteCodeHolder.uploadByteCodeForClass(transformClassName(className), byteCode);
        return byteCode;
    }

    private String transformClassName(String canonicalClassName) {
        return canonicalClassName.replace("/", ".");
    }
}
