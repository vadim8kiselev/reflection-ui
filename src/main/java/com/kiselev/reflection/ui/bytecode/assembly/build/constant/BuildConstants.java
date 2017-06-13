package com.kiselev.reflection.ui.bytecode.assembly.build.constant;

import java.io.File;

public class BuildConstants {

    public static final String JAR_COMMAND = "-cfvm";

    public static final String JAR = "\""
            + System.getProperty("java.home")
            + File.separator + ".."
            + File.separator + "bin"
            + File.separator + "jar.exe"
            + "\"";

    public static final String JAR_SUFFIX = ".jar";

    public static final String MANIFEST_SUFFIX = ".mf";

    public static final String CLASS_FILE_SUFFIX = ".class";

    public static final String JAR_C_COMMAND = "-C";
}
