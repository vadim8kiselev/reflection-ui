package com.classparser.bytecode.impl.saver;

import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.bytecode.impl.configuration.StateManager;
import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.exception.file.CreateFileException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

public class ByteCodeSaver {

    private static void writeByteCodeToFile(String fileName, byte[] byteCode) {
        try (FileOutputStream stream = new FileOutputStream(fileName)) {
            stream.write(byteCode);
        } catch (IOException exception) {
            String message = MessageFormat.format("Can't create file by path: {0}", fileName);
            throw new CreateFileException(message, exception);
        }
    }

    private static String getClassFileName(byte[] byteCode) {
        String classFileName = StateManager.getConfiguration().getDirectoryForSaveBytecode()
                + File.separatorChar
                + ClassNameUtils.getClassName(byteCode).replace('/', File.separatorChar);
        createClassFileNameDirectory(classFileName);
        return classFileName + Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    private static void createClassFileNameDirectory(String classFileName) {
        String path = classFileName.substring(0, classFileName.lastIndexOf(File.separatorChar));
        Path directoryPath = Paths.get(path);
        try {
            Files.createDirectories(directoryPath).toFile();
        } catch (IOException exception) {
            String message = MessageFormat.format("Directory: \"{0}\" can't created", path);
            throw new CreateFileException(message, exception);
        }
    }

    public void saveToFile(byte[] byteCode) {
        if (byteCode != null) {
            writeByteCodeToFile(getClassFileName(byteCode), byteCode);
        }
    }
}
