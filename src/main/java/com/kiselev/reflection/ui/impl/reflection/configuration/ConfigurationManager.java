package com.kiselev.reflection.ui.impl.reflection.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/14/2017.
 */
public class ConfigurationManager {

    private Map<String, Object> configuration = new HashMap<>();

    public ConfigurationManager() {
        configuration.putAll(getDefaultConfiguration());
    }

    public ConfigurationManager(Map<String, Object> configuration) {
        this.configuration.putAll(getDefaultConfiguration());
        this.configuration.putAll(configuration);
    }

    public boolean isShowAnnotationTypes() {
        return (boolean) configuration.get("sat");
    }

    public boolean isShowInnerClasses() {
        return (boolean) configuration.get("sic");
    }

    public boolean isShowNonJavaModifiers() {
        return (boolean) configuration.get("njm");
    }

    public boolean isShowDefaultValueInAnnotation() {
        return (boolean) configuration.get("dva");
    }

    public boolean isShowGenericSignatures() {
        return (boolean) configuration.get("sgs");
    }

    public boolean isShowVarArgs() {
        return (boolean) configuration.get("sva");
    }

    public boolean isDisplayFieldValue() {
        return (boolean) configuration.get("dvf");
    }

    public boolean isDisplayImports() {
        return (boolean) configuration.get("dim");
    }

    public boolean isShowClassFullName() {
        return (boolean) configuration.get("cfn");
    }

    public String getIndentSpaces() {
        return (String) configuration.get("cis");
    }

    public String getLineSeparator() {
        return (String) configuration.get("nlc");
    }

    private static Map<String, Object> getDefaultConfiguration() {
        HashMap<String, Object> configuration = new HashMap<>();
        configuration.put("sat", true);
        configuration.put("sic", true);
        configuration.put("njm", false);
        configuration.put("dva", false);
        configuration.put("sgs", true);
        configuration.put("sva", true);
        configuration.put("dvf", true);
        configuration.put("dim", true);
        configuration.put("cfn", false);
        configuration.put("cis", "    ");
        configuration.put("nlc", chooseSystemNewLineCharacter());

        return configuration;
    }

    private static String chooseSystemNewLineCharacter() {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            return "\n\r";
        } else {
            return "\n";
        }
    }
}
