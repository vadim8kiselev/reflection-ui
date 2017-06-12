package com.kiselev.reflection.ui.bytecode.assembly;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public class AgentAssembler {

    private static boolean assembled = false;

    public static void assembly() {
        try {
            if (!assembled) {
                // Build agent jar
                // String agentPath = AgentBuilder.build();
                String agentPath = "path\\to\\agent.jar";

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
