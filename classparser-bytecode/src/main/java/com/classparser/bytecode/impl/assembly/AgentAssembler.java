package com.classparser.bytecode.impl.assembly;

import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.impl.agent.Agent;
import com.classparser.bytecode.impl.agent.RetransformClassStorage;
import com.classparser.bytecode.impl.assembly.attach.AgentAttacher;
import com.classparser.bytecode.impl.assembly.build.AgentBuilder;

import java.io.File;

public class AgentAssembler {

    private static final String MANIFEST_AGENT_FILE_NAME = "AGENT-MANIFEST.MF";

    private static final String DEFAULT_AGENT_JAR_NAME = "agent.jar";

    public void assembly(JavaAgent agent) {
        if (!agent.isInitialize()) {
            String agentPath = AgentBuilder.getAgentPath(getAgentJarName());
            File file = new File(agentPath);
            if (!file.exists()) {
                agentPath = AgentBuilder.getBuilder()
                        .addAgentName(getAgentJarName())
                        .addAgentClass(getAgentClass())
                        .addManifest(getManifestFileName())
                        .addClasses(getAgentJarClasses())
                        .build();
            }

            AgentAttacher.attach(agentPath);
        }
    }

    protected String getAgentJarName() {
        return DEFAULT_AGENT_JAR_NAME;
    }

    protected Class<?> getAgentClass() {
        return Agent.class;
    }

    protected String getManifestFileName() {
        return MANIFEST_AGENT_FILE_NAME;
    }

    protected Class<?>[] getAgentJarClasses() {
        return new Class<?>[]{RetransformClassStorage.class, JavaAgent.class};
    }
}