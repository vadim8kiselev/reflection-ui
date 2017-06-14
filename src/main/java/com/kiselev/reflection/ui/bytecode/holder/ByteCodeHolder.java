package com.kiselev.reflection.ui.bytecode.holder;

import com.kiselev.reflection.ui.bytecode.assembly.AgentAssembler;
import com.kiselev.reflection.ui.bytecode.assembly.build.constant.Constants;
import com.sun.org.apache.regexp.internal.RE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
<<<<<<< Updated upstream
=======
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        if (!AgentAssembler.isAssembled()) {
            AgentAssembler.assembly();
        }

        String classFileName = getTypeName(clazz) + ".class";
        byte[] byteCode = byteCodeMap.get(clazz.getName());
=======
        loadByteCode();
        byte[] byteCode = byteCodeMap.get(clazz.getName());

        String classFileName = getClassFileName(clazz);
>>>>>>> Stashed changes
        writeByteCodeToFile(classFileName, byteCode);
        return "Bytecode was saved to file with name " + classFileName;
    }

<<<<<<< Updated upstream
    private static String getTypeName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();
=======
    private static void loadByteCode() {
        if (!AgentAssembler.isAssembled()) {
            AgentAssembler.assembly();
        }
    }

    private static String getClassFileName(Class<?> clazz) {
        String classFileName = "classes" + File.separator + clazz.getName().replace(".", File.separator);
        createClassFileNameDirectory(classFileName);
        return classFileName + Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    private static void createClassFileNameDirectory(String classFileName) {
        String path = System.getProperty(Constants.Properties.HOME_DIR) + File.separator
                + classFileName.substring(0, classFileName.lastIndexOf(File.separator));
        Path directoryPath = Paths.get(path);
>>>>>>> Stashed changes

        try {
            Files.createDirectories(directoryPath).toFile();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
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
