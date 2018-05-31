package com.classparser.bytecode.impl.assembly.attach;

import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.exception.ByteCodeParserException;
import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.URL;

public class AgentAttacher {

    private static final String VIRTUAL_MACHINE_CLASS_NAME = "com.sun.tools.attach.VirtualMachine.class";
    private static final char JAR_ENTRY_SEPARATOR = '/';
    private static final char JVM_NAME_ID_SEPARATOR = '@';
    private static final int BYTE_BUFFER_SIZE = 1024;

    private static final String CUSTOM_TOOL_FOLDER = "classes";
    private static final String ATTACH_CLASSES = "class-load.order";

    private final ClassDefiner classDefiner;

    public AgentAttacher(ClassDefiner classDefiner) {
        this.classDefiner = classDefiner;
    }

    public void attach(String agentPath) {
        File agentJar = new File(agentPath);
        if (agentJar.exists()) {
            if (isExistsToolJar()) {
                attachWithToolJar(agentPath);
            } else {
                attachWithoutToolJar(agentPath);
            }
        } else {
            throw new ByteCodeParserException("Could't find agent jar by follow path: " + agentPath);
        }
    }

    private boolean isExistsToolJar() {
        try {
            Class.forName(VIRTUAL_MACHINE_CLASS_NAME);
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private void attachWithToolJar(String agentPath) {
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

    public String getCurrentJVMProcessID() {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int processID = nameOfRunningVM.indexOf(JVM_NAME_ID_SEPARATOR);
        return nameOfRunningVM.substring(0, processID);
    }


    private void attachWithoutToolJar(String agentPath) {
        loadAttachClassesFromCustomTool();
        attachWithToolJar(agentPath);
    }

    private void loadAttachClassesFromCustomTool() {
        InputStream classLoadOrder = getResourceAsStream(ATTACH_CLASSES);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoadOrder))) {
            String className = reader.readLine();

            while (className != null) {
                String classFileName = createPathToClassFile(className, File.separatorChar);

                InputStream resourceAsStream;
                try {
                    resourceAsStream = getResourceAsStream(classFileName);
                } catch (ByteCodeParserException exception) {
                    classFileName = createPathToClassFile(className, JAR_ENTRY_SEPARATOR);
                    resourceAsStream = getResourceAsStream(classFileName);
                }

                byte[] bytes = readBytesFromInputStream(resourceAsStream);

                classDefiner.defineClass(bytes, getResource(classFileName));
                className = reader.readLine();
            }
        } catch (IOException exception) {
            throw new ByteCodeParserException("Problem occurred at loading classes from custom tool!", exception);
        }
    }

    private String createPathToClassFile(String className, char separator) {
        return CUSTOM_TOOL_FOLDER + separator +
                className.replace('.', separator) +
                Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    private byte[] readBytesFromInputStream(InputStream stream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int batchSize;
        byte[] data = new byte[BYTE_BUFFER_SIZE];
        try {
            batchSize = stream.read(data, 0, data.length);
            while (batchSize != -1) {
                buffer.write(data, 0, batchSize);
                batchSize = stream.read(data, 0, data.length);
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

    private static URL getResource(String fileName) {
        URL url = ClassLoader.getSystemResource(fileName);

        if (url == null) {
            throw new ByteCodeParserException("Can't load resource with name " + fileName);
        }

        return url;
    }
}