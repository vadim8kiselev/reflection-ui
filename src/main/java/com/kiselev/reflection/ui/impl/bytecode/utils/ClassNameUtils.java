package com.kiselev.reflection.ui.impl.bytecode.utils;

import com.kiselev.reflection.ui.exception.DecompilationException;
import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import jd.core.model.classfile.ConstantPool;
import jd.core.model.classfile.constant.*;
import jd.core.process.deserializer.ClassFormatException;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class ClassNameUtils {

    public static String getJavaBasedClassName(Class<?> clazz) {
        String className = clazz.getName();
        if (className.contains(Constants.Symbols.SLASH)) {
            className = className.substring(0, className.indexOf(Constants.Symbols.SLASH));
        }

        return className;
    }

    public static String normalizeSimpleName(String className) {
        String fullName = normalizeFullName(className);
        return fullName.substring(fullName.lastIndexOf(Constants.Symbols.DOT) + 1);
    }

    public static String normalizeFullName(String className) {
        if (className.endsWith(Constants.Suffix.CLASS_FILE_SUFFIX)) {
            className = className.replace(Constants.Suffix.CLASS_FILE_SUFFIX, "");
        }

        return className.replace(Constants.Symbols.SLASH, Constants.Symbols.DOT);
    }

    public static String getClassToFileName(Class<?> clazz) {
        return getJavaBasedClassName(clazz).replace(Constants.Symbols.DOT, Constants.Symbols.SLASH)
                + Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    public static String getSimpleName(Class<?> clazz) {
        String typeName = getJavaBasedClassName(clazz);
        return typeName.substring(typeName.lastIndexOf(Constants.Symbols.DOT) + 1);
    }

    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }

    public static String getClassName(byte[] bytecode) {
        try (DataInputStream stream = new DataInputStream(new ByteArrayInputStream(bytecode))) {
            if (stream.readInt() != -889275714) {
                throw new ClassFormatException("Invalid Java .class file");
            }

            stream.readUnsignedShort();
            stream.readUnsignedShort();
            Constant[] constants = readClassNameConstants(stream);
            ConstantPool constantPool = new ConstantPool(constants);
            stream.readUnsignedShort();
            return constantPool.getConstantClassName(stream.readUnsignedShort());
        } catch (IOException exception) {
            throw new DecompilationException("Can't get class name from bytes", exception);
        }
    }

    private static Constant[] readClassNameConstants(DataInput stream) throws IOException {
        Constant[] constants = new Constant[stream.readUnsignedShort()];

        for (int i = 1; i < constants.length; ++i) {
            byte tag = stream.readByte();
            switch (tag) {
                case 1:
                    constants[i] = new ConstantUtf8(tag, stream.readUTF());
                    break;
                case 7:
                    constants[i] = new ConstantClass(tag, stream.readUnsignedShort());
                    break;
                case 3: stream.readInt(); break;
                case 4: stream.readFloat(); break;
                case 5: stream.readLong(); break;
                case 6: stream.readDouble(); break;
                case 8: stream.readUnsignedShort(); break;
                case 9:
                case 10:
                case 11:
                case 12: stream.readUnsignedShort(); stream.readUnsignedShort();
            }
        }

        return constants;
    }
}
