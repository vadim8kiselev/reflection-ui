package com.kiselev.reflection.ui.bytecode.assembly.build;

import com.kiselev.reflection.ui.bytecode.assembly.build.action.Action;
import com.kiselev.reflection.ui.bytecode.assembly.build.action.FileDeleter;
import com.kiselev.reflection.ui.bytecode.assembly.build.constant.BuildConstants;
import com.kiselev.reflection.ui.bytecode.assembly.build.manifest.Manifest;

import java.io.File;
import java.io.IOException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class AgentJarBuilder {

    public static Builder getJarBuilder() {
        return new JarBuilder();
    }

    private static class JarBuilder implements Builder {

        private String commandPattern = "%s %s %s %s %s";

        private String jarName = "Agent.jar";

        private List<Class<?>> classes = new ArrayList<>();

        private Class<?> agentClass;

        private Manifest manifest;

        @Override
        public Builder addJarName(String name) {
            if (!name.contains(BuildConstants.JAR_SUFFIX)) {
                name = name + BuildConstants.JAR_SUFFIX;
            }

            this.jarName = name;
            return this;
        }

        @Override
        public Builder addClass(Class<?> clazz) {
            this.classes.add(clazz);
            return this;
        }

        @Override
        public Builder addManifest(Manifest manifest) {
            this.manifest = manifest;
            return this;
        }

        @Override
        public Builder addAgentClass(Class<?> clazz) {
            this.agentClass = clazz;
            return this;
        }

        @Override
        public String build() {
            return build(false);
        }

        @Override
        public String build(boolean isDeleteManifest) {
            if (agentClass == null) {
                //TODO: try find agentClass from classes
                throw new RuntimeException("Agent class must be");
            }

            if (manifest == null) {
                this.manifest = Manifest.createDefaultAgentManifest(agentClass);
            }

            String manifestPath = manifest.create();
            String command = String.format(commandPattern, BuildConstants.JAR, BuildConstants.JAR_COMMAND, jarName, manifestPath, toClassCommand());
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            String jarPath = System.getProperty("user.dir") + File.separator + jarName;

            if (isDeleteManifest) {
                FileDeleter.deleteAfterCreateFile(manifestPath, new Action<String>() {
                    @Override
                    public boolean isActionComplete(String data) {
                        File file = new File(data);
                        return file.isFile();
                    }
                }, jarPath);
            }

            File file = new File(jarPath);
            while (!file.exists());
            return jarPath;
        }

        private String toClassCommand() {
            StringBuilder builder = new StringBuilder();

            addClass(this.agentClass);
            for (Class<?> clazz : classes) {
                ProtectionDomain domain = clazz.getProtectionDomain();
                if (domain != null) {
                    String classFileProtectionDomain = domain.getCodeSource().getLocation().getPath().substring(1).replace("/", File.separator);
                    String classFilePath = clazz.getName().replace(".", File.separator) + BuildConstants.CLASS_FILE_SUFFIX;

                    builder.append(BuildConstants.JAR_C_COMMAND).append(" ")
                            .append(classFileProtectionDomain).append(" ")
                            .append(classFilePath).append(" ");
                }
            }

            return builder.toString();
        }
    }
}
