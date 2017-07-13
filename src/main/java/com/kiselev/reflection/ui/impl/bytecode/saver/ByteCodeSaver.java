package com.kiselev.reflection.ui.impl.bytecode.saver;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassNameResolver;
import com.kiselev.reflection.ui.impl.exception.file.CreateFileException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class ByteCodeSaver {

    public void saveToFile(Class<?> clazz, byte[] byteCode) {
        if (clazz != null && byteCode != null) {
            writeByteCodeToFile(getClassFileName(clazz), byteCode);
        }
    }

    private static void writeByteCodeToFile(String fileName, byte[] byteCode) {
        if (fileName != null && byteCode != null) {
            try (FileOutputStream stream = new FileOutputStream(fileName.replace(Constants.Symbols.DOLLAR, ""))) {
                stream.write(byteCode);
            } catch (IOException exception) {
                throw new CreateFileException("Can't create file by path: " + fileName, exception);
            }
        } else {
            throw new NullPointerException("Empty file name or byte code");
        }
    }

    private static String getClassFileName(Class<?> clazz) {
        String classFileName = "classes" + File.separator + ClassNameResolver.resolveJavaBasedClassName(clazz)
                .replace(Constants.Symbols.DOT, File.separator);
        createClassFileNameDirectory(classFileName);
        return classFileName + Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    private static void createClassFileNameDirectory(String classFileName) {
        String path = System.getProperty(Constants.Properties.HOME_DIR) + File.separator
                + classFileName.substring(0, classFileName.lastIndexOf(File.separator));
        Path directoryPath = Paths.get(path);
        try {
            Files.createDirectories(directoryPath).toFile();
        } catch (IOException exception) {
            throw new CreateFileException("Directory: " + path + "can't created", exception);
        }
    }
}
