package com.kiselev.reflection.ui.bytecode.assembly.build.manifest;

import com.kiselev.reflection.ui.bytecode.assembly.build.constant.BuildConstants;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Manifest {

    private String fileName;

    private Map<String, Object> parameters = new HashMap<>();

    public Manifest(String fileName) {
        if (!fileName.contains(BuildConstants.MANIFEST_SUFFIX)) {
            fileName = fileName + BuildConstants.MANIFEST_SUFFIX;
        }

        this.fileName = fileName;
        parameters.put("Manifest-Version", 1.0);
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public String create() {
        File file = new File(fileName);

        boolean isDelete = false;
        if (file.isFile()) {
            isDelete = file.delete();
        }

        try {
            boolean isFileCreate = file.createNewFile();

            if (isFileCreate) {
                try (PrintWriter out = new PrintWriter(fileName)) {
                    String context = getManifestContent();
                    out.print(context);
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return file.getAbsolutePath();
    }

    private String getManifestContent() {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return builder.toString();
    }

    public static Manifest createDefaultAgentManifest(Class<?> agentClass) {
        Manifest manifest = new Manifest("manifest");
        manifest.parameters.put("Agent-Class", agentClass.getName());
        manifest.parameters.put("Can-Retransform-Classes", true);

        return manifest;
    }
}
