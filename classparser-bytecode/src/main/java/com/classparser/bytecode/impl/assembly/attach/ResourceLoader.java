package com.classparser.bytecode.impl.assembly.attach;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {

    private final ClassLoader resourceClassLoader;

    private static final char JAR_ENTRY_SEPARATOR = '/';

    public ResourceLoader() {
        this(ClassLoader.getSystemClassLoader());
    }

    public ResourceLoader(ClassLoader resourceClassLoader) {
        this.resourceClassLoader = resourceClassLoader;
    }

    public InputStream getResourceAsStream(String fileName) {
        InputStream resourceStream = resourceClassLoader.getResourceAsStream(fileName);

        if (resourceStream == null) {
            fileName = fileName.replace(File.separatorChar, JAR_ENTRY_SEPARATOR);
            resourceStream = ClassLoader.getSystemResourceAsStream(fileName);
            if (resourceStream == null) {
                return null;
            }
        }

        return resourceStream;
    }

    public URL getResource(String fileName) {
        URL url = resourceClassLoader.getResource(fileName);

        if (url == null) {
            fileName = fileName.replace(File.separatorChar, JAR_ENTRY_SEPARATOR);
            url = ClassLoader.getSystemResource(fileName);
            if (url == null) {
                return null;
            }
        }

        return url;
    }
}
