package com.kiselev.reflection.ui.impl.bytecode.collector;

import com.kiselev.reflection.ui.impl.bytecode.utils.ClassFileUtils;
import com.kiselev.reflection.ui.impl.bytecode.utils.InnerClassesCollector;
import com.kiselev.reflection.ui.exception.file.ReadFileException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
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
            return readByteFromStream(stream);
        } catch (IOException exception) {
            throw new ReadFileException(String.format("Can't read file by path: %s", path), exception);
        }
    }

    private byte[] getByteCodeFromJar(String path) {
        String archiveName = ClassFileUtils.getArchivePath(path);
        String className = ClassFileUtils.getClassNameFromArchivePath(path);

        try {
            JarFile file = new JarFile(archiveName);
            JarEntry jarEntry = file.getJarEntry(className);
            try (InputStream fileStream = file.getInputStream(jarEntry)) {
                return readByteFromStream(fileStream);
            }
        } catch (Exception exception) {
            throw new ReadFileException(String.format("Can't read file by path: %s", path), exception);
        }
    }

    private byte[] readByteFromStream(InputStream stream) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            int read = stream.read();

            while (read != -1) {
                byteStream.write(read);
                read = stream.read();
            }

            return byteStream.toByteArray();
        } catch (IOException exception) {
            throw new ReadFileException("Can't read bytes from stream", exception);
        }
    }
}
