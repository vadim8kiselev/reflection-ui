package com.kiselev.reflection.ui.bytecode.assembly.build;

import com.kiselev.reflection.ui.bytecode.assembly.build.constant.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class AgentBuilder {

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String commandPattern = "%s %s %s %s %s";

        private String agentName = "agent.jar";

        private Class<?> agentClass;

        private String manifestName = "MANIFEST.mf";

        private List<Class<?>> attachedClasses = new ArrayList<>();

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
            try {
                Runtime.getRuntime().exec(convertCommand());
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        private String convertCommand() {
            return String.format(commandPattern,
                    Constants.Command.JAR_EXE_COMMAND,
                    Constants.Flag.JAR_FLAG,
                    agentName,
                    convertManifestPath(),
                    convertAttachedClasses());
        }

        private String convertManifestPath() {
            URL resource = getClass().getClassLoader().getResource("META-INF" + File.separator + manifestName);
            if (resource == null) {
                throw new RuntimeException("Manifest file cannot be null");
            }
            return resource.getPath();
        }

        private String convertAttachedClasses() {
            StringBuilder builder = new StringBuilder();

            attachedClasses.add(agentClass);
            for (Class<?> clazz : attachedClasses) {
                ProtectionDomain domain = clazz.getProtectionDomain();
                if (domain != null) {
                    String classFileProtectionDomain = domain.getCodeSource().getLocation().getPath().substring(1).replace("/", File.separator);
                    String classFilePath = clazz.getName().replace(".", File.separator) + Constants.Suffix.CLASS_FILE_SUFFIX;

                    builder.append(Constants.Flag.JAR_C_FLAG).append(Constants.Symbols.GAP)
                            .append(classFileProtectionDomain).append(Constants.Symbols.GAP)
                            .append(classFilePath).append(Constants.Symbols.GAP);
                }
            }

            return builder.toString();
        }

        private String retrieveAgentPath() {
            String agentJarPath = System.getProperty(Constants.Properties.HOME_DIR) + File.separator + agentName;
            waitForCreationOfFile(agentJarPath);
            return agentJarPath;
        }

        private void waitForCreationOfFile(String fileName) {
            File file = new File(fileName);
            while (!file.exists()) {
                // nothing
            }
        }
    }
}
