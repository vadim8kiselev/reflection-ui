package com.classparser.bytecode.impl.assembly;

import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.impl.agent.Agent;
import com.classparser.bytecode.impl.agent.RetransformClassStorage;
import com.classparser.bytecode.impl.assembly.attach.AgentAttacher;
import com.classparser.bytecode.impl.assembly.build.AgentBuilder;
import com.classparser.bytecode.impl.configuration.ConfigurationManager;

import java.io.File;

public class AgentAssembler {

    private static final String MANIFEST_AGENT_FILE_NAME = "AGENT-MANIFEST.MF";

    private static final String DEFAULT_AGENT_JAR_NAME = "agent.jar";

    private final AgentAttacher agentAttacher;

    private ConfigurationManager configurationManager;

    public AgentAssembler(AgentAttacher agentAttacher) {
        this.agentAttacher = agentAttacher;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public void assembly(JavaAgent agent) {
        if (!agent.isInitialize()) {
            String agentPath;
            if (isCacheAgentJar()) {
                String preparedAgentPath = AgentBuilder.getAgentPath(getAgentJarName());
                File file = new File(preparedAgentPath);
                if (!file.exists()) {
                    agentPath = createAgent();
                } else {
                    agentPath = preparedAgentPath;
                }
            } else {
                agentPath = createAgent();
            }

            agentAttacher.attach(agentPath);

            if (!isCacheAgentJar()) {
                File agentJar = new File(agentPath);
                if (!agentJar.exists() || !agentJar.delete()) {
                    System.err.println("Problems occurred with removing agent jar file: " + getAgentJarName());
                }
            }
        }
    }

    private boolean isCacheAgentJar() {
        if (configurationManager != null) {
            return configurationManager.isCacheAgentJar();
        }

        return false;
    }

    private String createAgent() {
        return AgentBuilder.getBuilder()
                .addAgentName(getAgentJarName())
                .addAgentClass(getAgentClass())
                .addManifest(getManifestFileName())
                .addClasses(getAgentJarClasses())
                .build();
    }

    protected String getAgentJarName() {
        return DEFAULT_AGENT_JAR_NAME;
    }

    protected Class<? extends JavaAgent> getAgentClass() {
        return Agent.class;
    }

    protected String getManifestFileName() {
        return MANIFEST_AGENT_FILE_NAME;
    }

    protected Class<?>[] getAgentJarClasses() {
        return new Class<?>[]{RetransformClassStorage.class, JavaAgent.class};
    }
}