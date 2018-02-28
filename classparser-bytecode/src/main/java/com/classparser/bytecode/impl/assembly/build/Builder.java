package com.classparser.bytecode.impl.assembly.build;

/**
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
     * <p>
     * if manifest is not found
     * append default manifest
     * </p>
     *
     * @param manifestPath - path to manifest
     */
    Builder addManifest(String manifestPath);

    /**
     * Add agent class
     * <p>
     * Agent class need contains method
     * with signature
     * static void agentmain(String args, Instrumentation instrumentation)
     * or
     * static void premain(String args, Instrumentation instrumentation)
     * </p>
     *
     * @param agentClass - agent class
     */
    Builder addAgentClass(Class<?> agentClass);

    /**
     * Process of building agent jar
     *
     * @return path to agent jar file
     */
    String build();
}
