package com.kiselev.reflection.ui.impl.bytecode.assembly.build;

/**
 * Created by Aleksei Makarov on 06/13/2017.
 *
 * Interface for creating agent jar files
 */
public interface Builder {

    /**
     * Name of jar file
     */
    Builder addAgentName(String agentName);

    /**
     * Add other classes append to jar
     */
    Builder addClasses(Class<?>... attachedClasses);

    /**
     * Add jar manifest
     * @param manifestName - path to manifest
     * <p>
     * if manifest is not found
     * append default manifest
     */
    Builder addManifest(String manifestName);

    /**
     * Add agent class
     * @param agentClass - agent class
     * <p>
     * Agent class need contains method
     * with signature
     * static void agentmain(String args, Instrumentation instrumentation)
     * or
     * static void premain(String args, Instrumentation instrumentation)
     */
    Builder addAgentClass(Class<?> agentClass);

    /**
     * Process of building agent jar
     *
     * @return path to agent jar file
     */
    String build();
}
