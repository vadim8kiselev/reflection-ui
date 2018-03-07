package com.classparser.bytecode.impl.assembly.build;

import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.bytecode.impl.collector.ClassFileByteCodeCollector;
import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.exception.agent.ClassLoadException;
import com.classparser.exception.agent.InvalidAgentClassException;
import com.classparser.exception.file.CreateFileException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public final class AgentBuilder {

    public static Builder getBuilder() {
        return new AgentJarBuilder();
    }

    public static String getAgentPath(String agentName) {
        return System.getProperty(Constants.Properties.HOME_DIR) + File.separatorChar + agentName;
    }

    private static class AgentJarBuilder implements Builder {

        private String agentName;

        private Class<?> agentClass;

        private String manifestName;

        private Collection<Class<?>> attachedClasses;

        private AgentJarBuilder() {
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

        public AgentJarBuilder addManifest(String manifestPath) {
            this.manifestName = appendSuffixIfNeeded(manifestPath, Constants.Suffix.MANIFEST_SUFFIX);
            return this;
        }

        public AgentJarBuilder addAgentClass(Class<?> agentClass) {
            if (!isAgentClass(agentClass)) {
                String exceptionMessage =
                        MessageFormat.format("Class \"{}\" is can't be a agent class", agentClass.getName());
                throw new InvalidAgentClassException(exceptionMessage);
            }
            this.agentClass = agentClass;
            return this;
        }

        public String build() {
            return createAgentJar();
        }

        private void findAgentClass() {
            if (this.agentClass == null) {
                for (Class<?> attachedClass : this.attachedClasses) {
                    if (isAgentClass(attachedClass)) {
                        this.agentClass = attachedClass;
                        return;
                    }
                }
            }
        }

        private boolean isAgentClass(Class<?> clazz) {
            if (clazz != null) {
                Method agentmain = getAgentMethod(clazz, "agentmain");
                Method premain = getAgentMethod(clazz, "premain");

                if (agentmain == null && premain == null) {
                    return false;
                }

                Method agentMethod = agentmain != null ? agentmain : premain;

                int modifiers = agentMethod.getModifiers();
                return Modifier.isStatic(modifiers) && agentMethod.getReturnType().equals(void.class);
            }

            return false;
        }

        private Method getAgentMethod(Class<?> clazz, String methodName) {
            try {
                return clazz.getDeclaredMethod(methodName, String.class, Instrumentation.class);
            } catch (NoSuchMethodException exception) {
                return null;
            }
        }

        private String appendSuffixIfNeeded(String value, String suffix) {
            if (!value.endsWith(suffix)) {
                return value + suffix;
            }

            return value;
        }

        private String createAgentJar() {
            findAgentClass();
            if (this.agentClass == null) {
                throw new NullPointerException("Agent class can't be null");
            }

            if (this.agentName == null) {
                this.agentName = "agent.jar";
            }

            String agentPath = getAgentPath(this.agentName);

            this.attachedClasses.add(this.agentClass);
            try (JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(agentPath), getManifest())) {
                ByteCodeCollector reader = new ClassFileByteCodeCollector();

                for (Class<?> attachedClass : this.attachedClasses) {
                    if (attachedClass != null) {
                        jarStream.putNextEntry(new JarEntry(ClassNameUtils.getClassToFileName(attachedClass)));

                        byte[] byteCode = reader.getByteCode(attachedClass);
                        if (byteCode == null) {
                            String exceptionMessage =
                                    MessageFormat.format("Class \"{}\" is not found!", attachedClass.getName());
                            throw new ClassLoadException(exceptionMessage);
                        }

                        jarStream.write(byteCode);
                        jarStream.flush();
                        jarStream.closeEntry();
                    }
                }
                jarStream.finish();
            } catch (IOException exception) {
                throw new CreateFileException("Agent jar is can't created", exception);
            }

            return agentPath;
        }

        private Manifest getManifest() {
            String manifestName = Constants.Folders.MANIFEST_HOME + File.separatorChar + this.manifestName;
            try (InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(manifestName)) {
                if (stream == null) {
                    Manifest manifest = new Manifest();
                    Attributes attributes = manifest.getMainAttributes();

                    attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
                    attributes.put(Constants.Manifest.RETRANSFORM, "true");
                    attributes.put(Constants.Manifest.REDEFINE, "true");
                    attributes.put(Constants.Manifest.AGENT_CLASS, agentClass.getName());

                    return manifest;
                }

                return new Manifest(stream);
            } catch (IOException exception) {
                String exceptionMessage =
                        MessageFormat.format("Manifest with name: \"{}\" is can't created", manifestName);
                throw new CreateFileException(exceptionMessage, exception);
            }
        }
    }
}