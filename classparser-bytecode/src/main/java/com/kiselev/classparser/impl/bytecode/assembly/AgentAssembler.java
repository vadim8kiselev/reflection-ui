package com.kiselev.classparser.impl.bytecode.assembly;

import com.kiselev.classparser.impl.bytecode.agent.Agent;
import com.kiselev.classparser.impl.bytecode.agent.ByteCodeHolder;
import com.kiselev.classparser.impl.bytecode.agent.JavaAgent;
import com.kiselev.classparser.impl.bytecode.agent.Transformer;
import com.kiselev.classparser.impl.bytecode.assembly.attach.AgentAttacher;
import com.kiselev.classparser.impl.bytecode.assembly.build.AgentBuilder;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public class AgentAssembler {

    private static boolean assembled = false;

    public void assembly() {
        if (!assembled) {
            // Build agent jar
            String agentPath = AgentBuilder.getBuilder()
                    .addAgentName(getAgentJarName())
                    .addAgentClass(getAgentClass())
                    .addManifest(getManifestFileName())
                    .addClasses(getAgentJarClasses())
                    .build();

            // Attach agent.jar to current process
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
