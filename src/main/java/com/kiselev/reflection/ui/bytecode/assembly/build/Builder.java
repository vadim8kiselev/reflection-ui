package com.kiselev.reflection.ui.bytecode.assembly.build;

/**
 * Created by Alexey Makarov on 06/13/2017.
 */
public interface Builder {

    Builder addAgentName(String agentName);

    Builder addClass(Class<?> attachedClass);

    Builder addManifest(String manifestName);

    Builder addAgentClass(Class<?> agentClass);

    String build();
}
