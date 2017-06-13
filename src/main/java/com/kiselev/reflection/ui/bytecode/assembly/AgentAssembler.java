package com.kiselev.reflection.ui.bytecode.assembly;

import com.kiselev.reflection.ui.bytecode.agent.Agent;
import com.kiselev.reflection.ui.bytecode.agent.Transformer;
import com.kiselev.reflection.ui.bytecode.assembly.attach.AgentAttacher;
import com.kiselev.reflection.ui.bytecode.assembly.build.AgentJarBuilder;
import com.kiselev.reflection.ui.bytecode.assembly.build.manifest.Manifest;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public class AgentAssembler {

    private static boolean assembled = false;

    public static void assembly() {
        try {
            if (!assembled) {
                // Build agent jar
                String agentPath = AgentJarBuilder
                        .getJarBuilder()
                        .addJarName("agent.jar")
                        .addAgentClass(Agent.class)
                        .addClass(Transformer.class)
                        .addManifest(Manifest.createDefaultAgentManifest(Agent.class))
                        .build(true);

                // Attach agent.jar to current process
                AgentAttacher.attach(agentPath);

                assembled = true;
            }
        } catch (Exception exception) {
            // Shit happens
            throw new RuntimeException(exception);
        }
    }

    public static boolean isAssembled() {
        return assembled;
    }
}
