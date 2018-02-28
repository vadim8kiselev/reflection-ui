package com.kiselev.classparser.impl.bytecode.assembly;

import com.kiselev.classparser.api.bytecode.agent.ByteCodeHolder;
import com.kiselev.classparser.api.bytecode.agent.JavaAgent;
import com.kiselev.classparser.impl.bytecode.agent.Agent;
import com.kiselev.classparser.impl.bytecode.agent.Transformer;
import com.kiselev.classparser.impl.bytecode.assembly.attach.AgentAttacher;
import com.kiselev.classparser.impl.bytecode.assembly.build.AgentBuilder;

import java.io.File;

public class AgentAssembler {

    private static boolean assembled = false;

    public void assembly() {
        if (!assembled) {
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
            assembled = true;
        }
    }

    protected String getAgentJarName() {
        return "agent.jar";
    }

    protected Class<?> getAgentClass() {
        return Agent.class;
    }

    protected String getManifestFileName() {
        return "REFLECTION-UI-MANIFEST.MF";
    }

    protected Class<?>[] getAgentJarClasses() {
        return new Class<?>[]{Transformer.class, JavaAgent.class, ByteCodeHolder.class};
    }

    public boolean isAssembled() {
        return assembled;
    }
}
