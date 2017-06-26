package com.kiselev.reflection.ui.impl.bytecode.agent;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.bytecode.holder.ByteCodeHolder;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
public final class Transformer implements ClassFileTransformer {

    @Override
    public final byte[] transform(ClassLoader loader,
                                  String className,
                                  Class<?> classBeingRedefined,
                                  ProtectionDomain protectionDomain,
                                  byte[] byteCode) throws IllegalClassFormatException {

        uploadByteCodeOfClassToHolder(className, byteCode);
        return byteCode;
    }

    private void uploadByteCodeOfClassToHolder(String className, byte[] byteCode) {
        String javaBasedClassName = transformClassName(className);
        ByteCodeHolder.uploadByteCodeForClass(javaBasedClassName, byteCode);
    }

    private String transformClassName(String canonicalClassName) {
        return canonicalClassName.replace(Constants.Symbols.SLASH, Constants.Symbols.DOT);
    }
}
