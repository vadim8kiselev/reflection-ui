package com.kiselev.reflection.ui.impl.reflection.configuration;

import com.kiselev.reflection.ui.configuration.reflection.ReflectionBuilderConfiguration;

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
        return ReflectionBuilderConfiguration
                .configure()
                .showAnnotationTypes(true)
                .showInnerClasses(true)
                .showNonJavaModifiers(false)
                .showDefaultValueInAnnotation(false)
                .displayValueForFields(true)
                .showGenericSignatures(true)
                .showVarArgs(true)
                .displayImports(true)
                .showClassFullName(false)
                .setCountIndentSpaces(4)
                .defineLineSeparator(chooseSystemNewLineCharacter())
                .getConfiguration();
    }

    private static String chooseSystemNewLineCharacter() {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            return "\n\r";
        } else {
            return "\n";
        }
    }
}
