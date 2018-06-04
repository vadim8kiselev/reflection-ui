package com.classparser.bytecode.impl.utils;

import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.exception.ByteCodeParserException;
import org.jetbrains.java.decompiler.main.DecompilerContext;
import org.jetbrains.java.decompiler.struct.consts.ConstantPool;
import org.jetbrains.java.decompiler.util.DataInputFullStream;

import java.io.IOException;
import java.util.Collections;

public class ClassNameUtils {

    public static final int MAGIC = 0xCAFEBABE;

    public static String getJavaBasedClassName(Class<?> clazz) {
        String className = clazz.getName();
        if (className.contains("/")) {
            className = className.substring(0, className.indexOf('/'));
        }

        return className;
    }

    public static String normalizeSimpleName(String className) {
        String fullName = normalizeFullName(className);
        return fullName.substring(fullName.lastIndexOf('.') + 1);
    }

    public static String normalizeFullName(String className) {
        if (className.endsWith(Constants.Suffix.CLASS_FILE_SUFFIX)) {
            className = ClassStringUtils.delete(className, Constants.Suffix.CLASS_FILE_SUFFIX);
        }

        return className.replace('/', '.');
    }

    public static String getClassToJarFileName(Class<?> clazz) {
        return getJavaBasedClassName(clazz).replace('.', '/')
                + Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    public static String getSimpleName(Class<?> clazz) {
        String typeName = getJavaBasedClassName(clazz);
        return typeName.substring(typeName.lastIndexOf('.') + 1);
    }

    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }

    public static String getClassName(byte[] bytecode) {
        try (DataInputFullStream stream = new DataInputFullStream(bytecode)) {
            if (stream.readInt() != MAGIC) {
                throw new ClassFormatError("Invalid java bytecode of class");
            }

            stream.discard(4);
            if (DecompilerContext.getCurrentContext() == null) {
                DecompilerContext.initContext(Collections.emptyMap());
            }
            ConstantPool pool = new ConstantPool(stream);
            stream.discard(2);
            int thisClassIdx = stream.readUnsignedShort();
            stream.discard(2);
            return pool.getPrimitiveConstant(thisClassIdx).getString();
        } catch (IOException exception) {
            throw new ByteCodeParserException("Constant pool is broken", exception);
        }
    }
}