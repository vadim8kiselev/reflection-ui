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

    public boolean isShowGenericSignatures(boolean flag) {
        return (boolean) configuration.put("sgs", flag);
    }

    public String getIndentSpaces() {
        return (String) configuration.get("cis");
    }

    public String getNewLineCharacter() {
        return (String) configuration.get("nlc");
    }

    private static Map<String, Object> getDefaultConfiguration() {
        HashMap<String, Object> configuration = new HashMap<>();
        configuration.put("sat", true);
        configuration.put("sic", true);
        configuration.put("njm", true);
        configuration.put("dva", true);
        configuration.put("sgs", true);
        configuration.put("cis", 4);
        configuration.put("nlc", chooseSystemNewLineCharacter());

        return configuration;
    }

    private static String chooseSystemNewLineCharacter() {
        if (System.getProperty("os").startsWith("windows")) {
            return "\n\r";
        } else {
            return "\n";
        }
    }
}
