package com.kiselev.reflection.ui.bytecode.assembly;

import com.kiselev.reflection.ui.bytecode.agent.Agent;
import com.kiselev.reflection.ui.bytecode.agent.Transformer;
import com.kiselev.reflection.ui.bytecode.assembly.attach.AgentAttacher;
import com.kiselev.reflection.ui.bytecode.assembly.build.AgentBuilder;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public class AgentAssembler {

    private static boolean assembled = false;

    public static void assembly() {
        try {
            if (!assembled) {
                // Build agent jar
                String agentPath = AgentBuilder.getBuilder()
                        .addAgentName("agent.jar")
                        .addAgentClass(Agent.class)
                        .addManifest("MANIFEST.MF")
                        .addClass(Transformer.class)
                        .build();

                // Attach agent.jar to current process
                AgentAttacher.attach(agentPath);

                assembled = true;
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static boolean isAssembled() {
        return assembled;
    }
}
