package com.kiselev.reflection.ui.impl.bytecode.assembly.build;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Created by Aleksei Makarov on 06/13/2017.
 */
public final class AgentBuilder {

    public static Builder getBuilder() {
        return new AgentJarBuilder();
    }

    private static class AgentJarBuilder implements Builder {

        private String agentName;

        private Class<?> agentClass;

        private String manifestName;

        private Collection<Class<?>> attachedClasses;

        AgentJarBuilder() {
        }

        public AgentJarBuilder addAgentName(String agentName) {
            this.agentName = appendSuffixIfNeeded(agentName, Constants.Suffix.JAR_SUFFIX);
            return this;
        }

        public AgentJarBuilder addClasses(Class<?>... attachedClasses) {
            if (this.attachedClasses == null) {
                this.attachedClasses = new HashSet<>();
            }
            this.attachedClasses.addAll(Arrays.asList(attachedClasses));
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
            return createAgentJar();
        }

        private String appendSuffixIfNeeded(String value, String suffix) {
            if (!value.endsWith(suffix)) {
                return value + suffix;
            }
            return value;
        }

        private String createAgentJar() {
            if (agentClass == null) {
                throw new RuntimeException("Agent class cannot be null");
            }
            if (agentName == null) {
                agentName = "agent.jar";
            }

            String agentPath = System.getProperty(Constants.Properties.HOME_DIR) + File.separator + agentName;

            attachedClasses.add(agentClass);
            try (JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(agentPath), getManifest())) {
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

            return agentPath;
        }

        private Manifest getManifest() {
            String manifest = Constants.Folders.MANIFEST_HOME + Constants.Symbols.SLASH + manifestName;
            try (InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(manifest)) {
                if (stream == null) {
                    //TODO : generate manifest file
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
    }
}