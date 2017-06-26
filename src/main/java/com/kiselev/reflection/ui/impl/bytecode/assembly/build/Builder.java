package com.kiselev.reflection.ui.impl.bytecode.assembly.build;

/**
 * Created by Aleksei Makarov on 06/13/2017.
 */
public interface Builder {

    Builder addAgentName(String agentName);

    Builder addClasses(Class<?>...attachedClasses);

    Builder addManifest(String manifestName);

    Builder addAgentClass(Class<?> agentClass);

    String build();
}
