package com.classparser.bytecode.impl.assembly.attach;

import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.exception.ByteCodeParserException;
import com.classparser.exception.file.ReadFileException;
import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class AgentAttacher {

    private static final String VIRTUAL_MACHINE_CLASS_NAME = "com.sun.tools.attach.VirtualMachine";

    private static final char JVM_NAME_ID_SEPARATOR = '@';

    private static final String CUSTOM_TOOLS = "attach-tools.jar";

    private static final String ATTACH_CLASSES = "classes$load.order";

    private static Method defineClass;

    public static void attach(String agentPath) {
        if (isExistsToolJar()) {
            attachWithToolJar(agentPath);
        } else {
            attachWithoutToolJar(agentPath);
        }
    }

    private static boolean isExistsToolJar() {
        try {
            Class.forName(VIRTUAL_MACHINE_CLASS_NAME);
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private static void attachWithToolJar(String agentPath) {
        String processID = getCurrentJVMProcessID();
        try {
            VirtualMachine virtualMachine = VirtualMachine.attach(processID);
            try {
                virtualMachine.loadAgent(agentPath);
            } finally {
                virtualMachine.detach();
            }
        } catch (Exception exception) {
            throw new ByteCodeParserException("Can't attach agent to JVM process!", exception);
        }
    }

    public static String getCurrentJVMProcessID() {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int processID = nameOfRunningVM.indexOf(JVM_NAME_ID_SEPARATOR);
        return nameOfRunningVM.substring(0, processID);
    }


    private static void attachWithoutToolJar(String agentPath) {
        loadAttachClassesFromCustomToolJar();
        attachWithToolJar(agentPath);
    }

    private static void loadAttachClassesFromCustomToolJar() {
        String resource = getResource(CUSTOM_TOOLS);
        Map<String, byte[]> byteCodes = new HashMap<>();
        try (JarFile jarFile = new JarFile(resource)) {
            Enumeration<JarEntry> classes = jarFile.entries();
            while (classes.hasMoreElements()) {
                JarEntry classEntry = classes.nextElement();
                int size = (int) classEntry.getSize();
                if (!classEntry.getName().contains("MANIFEST.MF")) {
                    try (InputStream stream = jarFile.getInputStream(classEntry)) {
                        byte[] classBytecode = new byte[size];
                        int read = stream.read(classBytecode);
                        if (read != 0) {
                            byteCodes.put(ClassNameUtils.normalizeFullName(classEntry.getName()), classBytecode);
                        }
                    }
                }
            }
        } catch (IOException exception) {
            throw new ReadFileException("Can't read file with custom tool jar!", exception);
        }

        for (byte[] byteCode : resolveOfOrderLoading(byteCodes)) {
            defineClass(byteCode);
        }
    }

    private static List<byte[]> resolveOfOrderLoading(Map<String, byte[]> orderMap) {
        List<byte[]> orderedClasses = new ArrayList<>();

        String resource = getResource(ATTACH_CLASSES);
        try (BufferedReader reader = new BufferedReader(new FileReader(resource))) {
            String className = reader.readLine();

            while (className != null) {
                orderedClasses.add(orderMap.get(className));
                className = reader.readLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return orderedClasses;
    }

    private static void defineClass(byte[] byteCode) {
        ClassLoader systemClassLoader = AgentAttacher.class.getClassLoader();
        if (defineClass == null) {
            try {
                Class<?>[] parameterTypes = {byte[].class, int.class, int.class};
                defineClass = ClassLoader.class.getDeclaredMethod("defineClass", parameterTypes);
                defineClass.setAccessible(true);
            } catch (ReflectiveOperationException exception) {
                throw new ByteCodeParserException("Can't obtains define class method!", exception);
            }
        }

        try {
            defineClass.invoke(systemClassLoader, byteCode, 0, byteCode.length);
        } catch (ReflectiveOperationException exception) {
            String className = ClassNameUtils.getClassName(byteCode);
            throw new ByteCodeParserException("Can't load class with name: " + className, exception);
        }
    }

    private static String getResource(String fileName) {
        URL systemResource = ClassLoader.getSystemResource(fileName);
        if (systemResource == null) {
            throw new ByteCodeParserException("Can't load resource with name " + fileName);
        }

        return systemResource.getFile();
    }
}
