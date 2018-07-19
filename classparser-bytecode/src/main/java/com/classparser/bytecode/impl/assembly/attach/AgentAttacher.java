package com.classparser.bytecode.impl.assembly.attach;

import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.bytecode.impl.utils.IOUtils;
import com.classparser.exception.ByteCodeParserException;
import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

public class AgentAttacher {

    private static final String VIRTUAL_MACHINE_CLASS_NAME = "com.sun.tools.attach.VirtualMachine";
    private static final char JVM_NAME_ID_SEPARATOR = '@';

    private static final String CUSTOM_TOOL_FOLDER = "classes";
    private static final String ATTACH_CLASSES = "attach.classes";

    private final ClassDefiner classDefiner;

    private final ResourceLoader resourceLoader;

    public AgentAttacher(ClassDefiner classDefiner, ResourceLoader resourceLoader) {
        this.classDefiner = classDefiner;
        this.resourceLoader = resourceLoader;
    }

    public void attach(String agentPath) {
        attach(agentPath, "");
    }

    public void attach(String agentPath, String parameters) {
        File agentJar = new File(agentPath);
        if (agentJar.exists()) {
            if (isExistsToolJar()) {
                attachWithToolJar(agentPath, parameters);
            } else {
                attachWithoutToolJar(agentPath, parameters);
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

    private void attachWithToolJar(String agentPath, String parameters) {
        String processID = getCurrentJVMProcessID();
        try {
            VirtualMachine virtualMachine = VirtualMachine.attach(processID);
            try {
                virtualMachine.loadAgent(agentPath, parameters);
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

    private void attachWithoutToolJar(String agentPath, String parameters) {
        loadAttachClassesFromCustomTool();
        attachWithToolJar(agentPath, parameters);
    }

    private void loadAttachClassesFromCustomTool() {
        InputStream classLoadOrder = resourceLoader.getResourceAsStream(ATTACH_CLASSES);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoadOrder))) {
            String className;
            while ((className = reader.readLine()) != null) {
                String classFileName = CUSTOM_TOOL_FOLDER + File.separatorChar +
                        className.replace('.', File.separatorChar) +
                        Constants.Suffix.CLASS_FILE_SUFFIX;

                InputStream resourceAsStream = resourceLoader.getResourceAsStream(classFileName);
                byte[] bytes = IOUtils.readBytesFromInputStream(resourceAsStream);

                classDefiner.defineClass(bytes, resourceLoader.getResource(classFileName));
            }
        } catch (IOException exception) {
            throw new ByteCodeParserException("Problem occurred at loading classes from custom tool!", exception);
        }
    }
}