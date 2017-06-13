package com.kiselev.reflection.ui.bytecode.assembly.build.constant;

import java.io.File;

public class Constants {

    public static class Flag {

        public static final String JAR_FLAG = "-cfvm";

        public static final String JAR_C_FLAG = "-C";
    }

    public static class Command {

        public static final String JAR_EXE_COMMAND = "\""
                + System.getProperty("java.home")
                + File.separator + ".."
                + File.separator + "bin"
                + File.separator + "jar.exe"
                + "\"";
    }

    public static class Suffix {

        public static final String JAR_SUFFIX = ".jar";

        public static final String MANIFEST_SUFFIX = ".mf";

        public static final String CLASS_FILE_SUFFIX = ".class";
    }

    public static class Symbols {

        public static final String GAP = " ";
    }
}
