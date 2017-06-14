package com.kiselev.reflection.ui.bytecode.holder;

import com.kiselev.reflection.ui.bytecode.assembly.AgentAssembler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public class ByteCodeHolder {

    private static Map<String, byte[]> byteCodeMap = new HashMap<>();

    public static void uploadByteCodeForClass(String className, byte[] byteCode) {
        byteCodeMap.put(className, byteCode);
    }

    public static String getDecompilledByteCode(Class<?> clazz) {
        loadByteCode();
        byte[] byteCode = byteCodeMap.get(clazz.getName());

        String classFileName = getTypeName(clazz) + ".class";
        writeByteCodeToFile(classFileName, byteCode);

        return "Bytecode was saved to file with name " + classFileName;
    }

    private static void loadByteCode() {
        if (!AgentAssembler.isAssembled()) {
            AgentAssembler.assembly();
        }
    }

    private static String getTypeName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();

        if ("".equals(typeName)) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
        }

        return typeName;
    }

    private static void writeByteCodeToFile(String fileName, byte[] byteCode) {
        if (fileName != null && byteCode != null) {
            try (FileOutputStream stream = new FileOutputStream(fileName)) {
                stream.write(byteCode);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        } else {
            throw new RuntimeException("Empty file name or byte code");
        }
    }
}
