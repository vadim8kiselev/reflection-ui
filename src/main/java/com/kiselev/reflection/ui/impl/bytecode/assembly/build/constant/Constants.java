package com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant;

/**
 * Created by Vadim Kiselev on 06/14/2017.
 */
public interface Constants {

    interface Folders {

        String MANIFEST_HOME = "META-INF";
    }

    interface Properties {

        String HOME_DIR = "user.dir";
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
