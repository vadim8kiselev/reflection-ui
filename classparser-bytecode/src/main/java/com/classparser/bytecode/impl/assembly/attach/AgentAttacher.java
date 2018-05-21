package com.classparser.bytecode.impl.assembly.attach;

import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.exception.ByteCodeParserException;
import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

public final class AgentAttacher {

    private static final String VIRTUAL_MACHINE_CLASS_NAME = "com.sun.tools.attach.VirtualMachine.class";
    private static final char JAR_ENTRY_SEPARATOR = '/';
    private static final char JVM_NAME_ID_SEPARATOR = '@';
    private static final int BYTE_BUFFER_SIZE = 1024;

    private static final String CUSTOM_TOOLS = "classes";
    private static final String ATTACH_CLASSES = "class-load.order";

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
        InputStream classLoadOrder = getResourceAsStream(ATTACH_CLASSES);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoadOrder))) {
            String className = reader.readLine();

            while (className != null) {
                String classFileName = CUSTOM_TOOLS + JAR_ENTRY_SEPARATOR +
                        className.replace('.', JAR_ENTRY_SEPARATOR) +
                        Constants.Suffix.CLASS_FILE_SUFFIX;

                InputStream resourceAsStream = getResourceAsStream(classFileName);
                byte[] bytes = readBytesFromInputStream(resourceAsStream);
                defineClass(bytes);

                className = reader.readLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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

    private static byte[] readBytesFromInputStream(InputStream stream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int batchSize;
        byte[] data = new byte[BYTE_BUFFER_SIZE];
        try {
            while ((batchSize = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, batchSize);
            }

            buffer.flush();
        } catch (IOException exception) {
            throw new ByteCodeParserException("Occurred problems at class loading!", exception);
        }

        return buffer.toByteArray();
    }

    private static InputStream getResourceAsStream(String fileName) {
        InputStream resourceStream = ClassLoader.getSystemResourceAsStream(fileName);

        if (resourceStream == null) {
            throw new ByteCodeParserException("Can't load resource with name " + fileName);
        }

        return resourceStream;
    }
}
