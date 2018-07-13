package com.classparser.bytecode.impl.collector;

import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.impl.utils.ClassFileUtils;
import com.classparser.bytecode.impl.utils.IOUtils;
import com.classparser.exception.file.ReadFileException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFileByteCodeCollector implements ByteCodeCollector {

    public byte[] getByteCode(Class<?> clazz) {
        String filePath = ClassFileUtils.getFilePath(clazz);

        if (filePath.isEmpty()) {
            return null;
        }

        if (ClassFileUtils.isArchive(filePath)) {
            return getByteCodeFromJar(filePath);
        } else {
            return getByteCodeFromFile(filePath);
        }
    }

    private byte[] getByteCodeFromFile(String path) {
        try (FileInputStream stream = new FileInputStream(path)) {
            return IOUtils.readBytesFromInputStream(stream);
        } catch (IOException exception) {
            String message = MessageFormat.format("Can't read file by path: {0}", path);
            throw new ReadFileException(message, exception);
        }
    }

    private byte[] getByteCodeFromJar(String path) {
        String archiveName = ClassFileUtils.getArchivePath(path);
        String className = ClassFileUtils.getClassNameFromArchivePath(path);

        try {
            JarFile file = new JarFile(archiveName);
            JarEntry jarEntry = file.getJarEntry(className);
            try (InputStream fileStream = file.getInputStream(jarEntry)) {
                return IOUtils.readBytesFromInputStream(fileStream);
            }
        } catch (Exception exception) {
            String message = MessageFormat.format("Can't read file by path: {0}", path);
            throw new ReadFileException(message, exception);
        }
    }
}