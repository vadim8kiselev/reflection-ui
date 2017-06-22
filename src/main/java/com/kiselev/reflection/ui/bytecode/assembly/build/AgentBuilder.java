package com.kiselev.reflection.ui.bytecode.assembly.build;

import com.kiselev.reflection.ui.bytecode.assembly.build.constant.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Created by Алексей on 22.06.2017.
 */
public class AgentBuilder {

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String agentName = "agent.jar";

        private Class<?> agentClass;

        private String manifestName = "MANIFEST.MF";

        private List<Class<?>> attachedClasses = new ArrayList<>();

        private static final long FAILED_TIME = 3000L;

        public Builder addAgentName(String agentName) {
            if (!agentName.endsWith(Constants.Suffix.JAR_SUFFIX)) {
                agentName = agentName + Constants.Suffix.JAR_SUFFIX;
            }
            this.agentName = agentName;
            return this;
        }

        public Builder addClass(Class<?> attachedClass) {
            this.attachedClasses.add(attachedClass);
            return this;
        }

        public Builder addManifest(String manifestName) {
            if (!manifestName.endsWith(Constants.Suffix.MANIFEST_SUFFIX)) {
                manifestName = manifestName + Constants.Suffix.MANIFEST_SUFFIX;
            }
            this.manifestName = manifestName;
            return this;
        }

        public Builder addAgentClass(Class<?> agentClass) {
            this.agentClass = agentClass;
            return this;
        }

        public String build() {
            createAgentJar();
            return retrieveAgentPath();
        }

        private void createAgentJar() {
            if (agentClass == null) {
                throw new RuntimeException("Agent class cannot be null");
            }

            attachedClasses.add(agentClass);
            try (JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(getAgentPath()), getManifest())) {
                for (Class<?> attachedClass : attachedClasses) {
                    jarStream.putNextEntry(new JarEntry(getSaveClassName(attachedClass)));
                    jarStream.write(getClassBytes(attachedClass));
                    jarStream.flush();
                }
                jarStream.finish();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        private Manifest getManifest() {
            try {
                InputStream stream = ClassLoader.getSystemClassLoader()
                        .getResourceAsStream("META-INF/" + manifestName.toUpperCase());
                if (stream == null) {
                    throw new RuntimeException("Manifest file cannot be null");
                }
                return new Manifest(stream);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        private String getAgentPath() {
            return System.getProperty(Constants.Properties.HOME_DIR) + File.separator + agentName;
        }

        private String retrieveAgentPath() {
            String agentJarPath = getAgentPath();
            waitForCreationOfFile(agentJarPath);
            return agentJarPath;
        }

        private void waitForCreationOfFile(String fileName) {
            File file = new File(fileName);
            long time = System.currentTimeMillis();

            while (!file.exists()) {
                if (System.currentTimeMillis() > time + FAILED_TIME) {
                    throw new RuntimeException("Creating jar agent is failed");
                }
            }
        }

        private String getSaveClassName(Class<?> clazz) {
            return clazz.getName().replace(".", File.separator) + Constants.Suffix.CLASS_FILE_SUFFIX;
        }

        private byte[] getClassBytes(Class<?> clazz) {
            String classFileName = getSaveClassName(clazz);
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(classFileName);
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
    }
}


