package com.kiselev.reflection.ui.impl.bytecode.saver;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.bytecode.configuration.StateManager;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassNameUtils;
import com.kiselev.reflection.ui.exception.file.CreateFileException;

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

    public void saveToFile(byte[] byteCode) {
        if (byteCode != null) {
            writeByteCodeToFile(getClassFileName(byteCode), byteCode);
        }
    }

    private static void writeByteCodeToFile(String fileName, byte[] byteCode) {
        try (FileOutputStream stream = new FileOutputStream(fileName)) {
            stream.write(byteCode);
        } catch (IOException exception) {
            throw new CreateFileException(String.format("Can't create file by path: %s", fileName), exception);
        }
    }

    private static String getClassFileName(byte[] byteCode) {
        String classFileName = StateManager.getConfiguration().getDirectoryForSaveBytecode()
                + File.separator
                + ClassNameUtils.getClassName(byteCode).replace(Constants.Symbols.SLASH, File.separator);
        createClassFileNameDirectory(classFileName);
        return classFileName + Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    private static void createClassFileNameDirectory(String classFileName) {
        String path = classFileName.substring(0, classFileName.lastIndexOf(File.separator));
        Path directoryPath = Paths.get(path);
        try {
            Files.createDirectories(directoryPath).toFile();
        } catch (IOException exception) {
            throw new CreateFileException(String.format("Directory: %s can't created", path), exception);
        }
    }
}
