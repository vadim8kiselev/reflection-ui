package com.kiselev.reflection.ui.impl.bytecode.collector;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassNameResolver;
import com.kiselev.reflection.ui.impl.bytecode.utils.InnerClassesCollector;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class ClassFileByteCodeCollector implements ByteCodeCollector {

    public byte[] getByteCode(Class<?> clazz) {
        String filePath = getFilePath(clazz);

        if (filePath == null) {
            return null;
        }

        if (getArchiveType(filePath).isEmpty()) {
            return getByteCodeFromFile(filePath);
        } else {
            return getByteCodeFromJar(filePath);
        }
    }

    @Override
    public List<byte[]> getByteCodeOfInnerClasses(Class<?> clazz) {
        List<byte[]> innerClasses = new ArrayList<>();

        for (Class<?> innerClass : InnerClassesCollector.getInnerClasses(clazz)) {
            byte[] byteCode = getByteCode(innerClass);
            if (byteCode != null) {
                innerClasses.add(byteCode);
            }
        }

        return innerClasses;
    }

    private byte[] getByteCodeFromFile(String path) {
        try (FileInputStream stream = new FileInputStream(path)) {
            return readByteFromStream(stream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private byte[] getByteCodeFromJar(String path) {
        String archiveType = getArchiveType(path);
        int dividePosition = path.lastIndexOf(Constants.Symbols.DOT + archiveType + "!")
                + archiveType.length() + 1;
        String archiveName = path.substring(0, dividePosition)
                .replace(Constants.Symbols.SLASH, "\\").replace("%20", " ");
        String className = path.substring(dividePosition + 2, path.length());

        try {
            URL urlJarFile = new URL(archiveName);
            JarFile file = new JarFile(urlJarFile.getFile());
            JarEntry jarEntry = file.getJarEntry(className);
            try (InputStream fileStream = file.getInputStream(jarEntry)) {
                return readByteFromStream(fileStream);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private byte[] readByteFromStream(InputStream stream) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            int reads = stream.read();

            while (reads != -1) {
                byteStream.write(reads);
                reads = stream.read();
            }

            return byteStream.toByteArray();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String getArchiveType(String path) {
        if (path.contains(".jar!")) return "jar";
        if (path.contains(".war!")) return "war";
        if (path.contains(".ear!")) return "ear";
        if (path.contains(".zip!")) return "zip";

        return "";
    }

    private String getFilePath(Class<?> clazz) {
        ClassLoader loader = clazz.getClassLoader();

        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
            while (loader != null && loader.getParent() != null) {
                loader = loader.getParent();
            }
        }

        if (loader != null) {
            URL resource = loader.getResource(ClassNameResolver.resolveClassFileName(clazz));
            if (resource != null) {
                return resource.getFile();
            }
        }

        return null;
    }
}
