package com.kiselev.reflection.ui.bytecode.assembly.build;

import com.kiselev.reflection.ui.bytecode.assembly.build.manifest.Manifest;

public interface Builder {

    Builder addJarName(String name);

    Builder addClass(Class<?> clazz);

    Builder addAgentClass(Class<?> clazz);

    Builder addManifest(Manifest manifest);

    String build(boolean isDeleteManifest);

    String build();
}
