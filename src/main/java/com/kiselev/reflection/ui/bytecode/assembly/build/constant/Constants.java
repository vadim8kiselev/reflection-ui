package com.kiselev.reflection.ui.bytecode.assembly.build.constant;

import java.io.File;

public class Constants {

    public static class Flag {

        public static final String JAR_FLAG = "-cfvm";

        public static final String JAR_C_FLAG = "-C";
    }

    public static class Properties {

        public static final String HOME_DIR = "user.dir";
    }

    public static class Command {

        public static final String JAR_EXE_COMMAND = "\""
                + System.getProperty("java.home")
                + (!isJava9() ? File.separator + ".." : "")
                + File.separator + "bin"
                + File.separator + "jar.exe"
                + "\"";

        private static boolean isJava9() {
            return System.getProperty("java.version").startsWith("9")
                    || System.getProperty("java.version").startsWith("1.9");
        }
    }

    public static class Suffix {

        public static final String JAR_SUFFIX = ".jar";

        public static final String MANIFEST_SUFFIX = ".MF";

        public static final String CLASS_FILE_SUFFIX = ".class";
    }

    public static class Symbols {

        public static final String GAP = " ";
    }
}
