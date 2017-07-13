package com.kiselev.reflection.ui.impl.bytecode.assembly.build;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.collector.ClassFileByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassNameUtils;
import com.kiselev.reflection.ui.impl.exception.agent.InvalidAgentClassException;
import com.kiselev.reflection.ui.impl.exception.file.CreateFileException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.Attributes;
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
            if (!isAgentClass(agentClass)) {
                throw new InvalidAgentClassException("Class " + agentClass.getName() + " is can't be a agent class");
            }
            this.agentClass = agentClass;
            return this;
        }

        public String build() {
            return createAgentJar();
        }

        private void findAgentClass() {
            if (agentClass == null) {
                for (Class<?> attachedClass : attachedClasses) {
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
            if (agentClass == null) {
                throw new NullPointerException("Agent class can't be null");
            }

            if (agentName == null) {
                agentName = "agent.jar";
            }

            String agentPath = System.getProperty(Constants.Properties.HOME_DIR) + File.separator + agentName;

            attachedClasses.add(agentClass);
            try (JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(agentPath), getManifest())) {
                ByteCodeCollector reader = new ClassFileByteCodeCollector();

                for (Class<?> attachedClass : attachedClasses) {
                    if (attachedClass != null) {
                        jarStream.putNextEntry(new JarEntry(ClassNameUtils.resolveClassFileName(attachedClass)));

                        byte[] byteCode = reader.getByteCode(attachedClass);
                        if (byteCode == null) {
                            throw new RuntimeException("Class is not found!");
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
            String manifestName = Constants.Folders.MANIFEST_HOME + Constants.Symbols.SLASH + this.manifestName;
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
                String message = "Manifest with name: \"" + manifestName + "\" is can't created";
                throw new CreateFileException(message, exception);
            }
        }
    }
}