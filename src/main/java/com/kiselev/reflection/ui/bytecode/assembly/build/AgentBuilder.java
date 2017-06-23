package com.kiselev.reflection.ui.bytecode.assembly.build;

import com.kiselev.reflection.ui.bytecode.agent.Agent;
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
 * Created by Aleksei Makarov on 06/13/2017.
 */
public class AgentBuilder {

    public static Builder getBuilder() {
        return new AgentJarBuilder();
    }

    public static class AgentJarBuilder implements Builder {

        private String agentName = "agent.jar";

        private Class<?> agentClass = Agent.class;

        private String manifestName = "MANIFEST.mf";

        private List<Class<?>> attachedClasses = new ArrayList<>();

        private long CREATION_TIMEOUT = 3000L;

        public AgentJarBuilder addAgentName(String agentName) {
            this.agentName = appendSuffixIfNeeded(agentName, Constants.Suffix.JAR_SUFFIX);
            return this;
        }

        public AgentJarBuilder addClass(Class<?> attachedClass) {
            this.attachedClasses.add(attachedClass);
            return this;
        }

        public AgentJarBuilder addManifest(String manifestName) {
            this.manifestName = appendSuffixIfNeeded(manifestName, Constants.Suffix.MANIFEST_SUFFIX);
            return this;
        }

        public AgentJarBuilder addAgentClass(Class<?> agentClass) {
            this.agentClass = agentClass;
            return this;
        }

        public String build() {
            createAgentJar();
            return retrieveAgentPath();
        }

        private String appendSuffixIfNeeded(String value, String suffix) {
            if (!value.endsWith(suffix)) {
                return value + suffix;
            }
            return value;
        }

        private void createAgentJar() {
            if (agentClass == null) {
                throw new RuntimeException("Agent class cannot be null");
            }

            attachedClasses.add(agentClass);
            try (JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(getAgentPath()), getManifest())) {
                for (Class<?> attachedClass : attachedClasses) {
                    jarStream.putNextEntry(new JarEntry(getClassFileName(attachedClass)));
                    jarStream.write(getClassBytes(attachedClass));
                    jarStream.flush();
                    jarStream.closeEntry();
                }
                jarStream.finish();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        // TODO: CHECK
        private Manifest getManifest() {
            String manifest = Constants.Folders.MANIFEST_HOME + Constants.Symbols.SLASH + manifestName;
            try (InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(manifest)) {
                if (stream == null) {
                    throw new RuntimeException("Manifest file cannot be null");
                }
                return new Manifest(stream);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        private byte[] getClassBytes(Class<?> clazz) {
            String classFileName = getClassFileName(clazz);
            try (InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(classFileName);
                 ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
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

        private String getClassFileName(Class<?> clazz) {
            return clazz.getName().replace(Constants.Symbols.DOT, Constants.Symbols.SLASH)
                    + Constants.Suffix.CLASS_FILE_SUFFIX;
        }

        private String retrieveAgentPath() {
            String agentJarPath = getAgentPath();
            waitForCreationOfFile(agentJarPath);
            return agentJarPath;
        }

        private String getAgentPath() {
            return System.getProperty(Constants.Properties.HOME_DIR) + File.separator + agentName;
        }

        // TODO: CHECK
        private void waitForCreationOfFile(String fileName) {
            File file = new File(fileName);
            long timeout = System.currentTimeMillis() + CREATION_TIMEOUT;

            while (!file.exists()) {
                if (System.currentTimeMillis() > timeout) {
                    throw new RuntimeException("Creating jar agent is failed");
                }
            }
        }
    }
}