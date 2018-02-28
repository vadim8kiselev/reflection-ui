package com.classparser.bytecode.impl.assembly.build.constant;

import java.util.jar.Attributes;

public interface Constants {

    interface Folders {

        String MANIFEST_HOME = "META-INF";
    }

    interface Properties {

        String HOME_DIR = "user.dir";
    }

    interface Manifest {

        Attributes.Name RETRANSFORM = new Attributes.Name("Can-Retransform-Classes");

        Attributes.Name REDEFINE = new Attributes.Name("Can-Redefine-Classes");

        Attributes.Name AGENT_CLASS = new Attributes.Name("Agent-Class");
    }

    interface Suffix {

        String JAR_SUFFIX = ".jar";

        String MANIFEST_SUFFIX = ".MF";

        String CLASS_FILE_SUFFIX = ".class";
    }

    interface Symbols {

        String DOLLAR = "$";

        String SLASH = "/";

        String DOT = ".";
    }
}
