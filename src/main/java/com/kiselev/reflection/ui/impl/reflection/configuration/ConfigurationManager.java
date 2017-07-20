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
        this();
        this.configuration.putAll(configuration);
    }

    private boolean isInstance(Object object, Class<?> clazz) {
        return clazz.isInstance(object);
    }

    public boolean isShowAnnotationTypes() {
        Object config = configuration.get("sat");
        if (isInstance(config, boolean.class)) {
            return (boolean) configuration.get("sat");
        } else {
            return (boolean) getDefaultConfiguration().get("sat");
        }
    }

    public boolean isShowInnerClasses() {
        Object config = configuration.get("sic");
        if (isInstance(config, boolean.class)) {
            return (boolean) configuration.get("sic");
        } else {
            return (boolean) getDefaultConfiguration().get("sic");
        }
    }

    public boolean isShowNonJavaModifiers() {
        Object config = configuration.get("njm");
        if (isInstance(config, boolean.class)) {
            return (boolean) configuration.get("njm");
        } else {
            return (boolean) getDefaultConfiguration().get("njm");
        }
    }

    public boolean isShowDefaultValueInAnnotation() {
        Object config = configuration.get("dva");
        if (isInstance(config, boolean.class)) {
            return (boolean) configuration.get("dva");
        } else {
            return (boolean) getDefaultConfiguration().get("dva");
        }
    }

    public boolean isShowGenericSignatures() {
        Object config = configuration.get("sgs");
        if (isInstance(config, boolean.class)) {
            return (boolean) configuration.get("sgs");
        } else {
            return (boolean) getDefaultConfiguration().get("sgs");
        }
    }

    public boolean isShowVarArgs() {
        Object config = configuration.get("sva");
        if (isInstance(config, boolean.class)) {
            return (boolean) configuration.get("sva");
        } else {
            return (boolean) getDefaultConfiguration().get("sva");
        }
    }

    public boolean isDisplayFieldValue() {
        Object config = configuration.get("dvf");
        if (isInstance(config, boolean.class)) {
            return (boolean) configuration.get("dvf");
        } else {
            return (boolean) getDefaultConfiguration().get("dvf");
        }
    }

    public boolean isDisplayImports() {
        Object config = configuration.get("dim");
        if (isInstance(config, boolean.class)) {
            return (boolean) configuration.get("dim");
        } else {
            return (boolean) getDefaultConfiguration().get("dim");
        }
    }

    public boolean isShowClassFullName() {
        Object config = configuration.get("cfn");
        if (isInstance(config, boolean.class)) {
            return (boolean) configuration.get("cfn");
        } else {
            return (boolean) getDefaultConfiguration().get("cfn");
        }
    }

    public String getIndentSpaces() {
        Object config = configuration.get("cis");
        if (isInstance(config, String.class)) {
            return (String) configuration.get("cis");
        } else {
            return (String) getDefaultConfiguration().get("cis");
        }
    }

    public String getLineSeparator() {
        Object config = configuration.get("nlc");
        if (isInstance(config, String.class)) {
            return (String) configuration.get("nlc");
        } else {
            return (String) getDefaultConfiguration().get("nlc");
        }
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
