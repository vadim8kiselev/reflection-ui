package com.classparser.bytecode.impl.decompile.jd.configuration;

import com.classparser.bytecode.impl.decompile.jd.JDDecompiler;
import com.classparser.configuration.Configuration;

/**
 * Created by Aleksei Makarov on 07/19/2017.
 * <p>
 * Builder configuration for
 *
 * @see JDDecompiler
 */
public interface JDConfiguration extends Configuration {

    /**
     * Show default constructor
     * <p>
     * Default value: true
     */
    JDConfiguration showDefaultConstructor(boolean flag);

    /**
     * Realignment line number
     * <p>
     * Default value: true
     */
    JDConfiguration realignmentLineNumber(boolean flag);

    /**
     * Show prefix "this"
     * <p>
     * Default value: true
     */
    JDConfiguration showPrefixThis(boolean flag);

    /**
     * Merge empty lines
     * <p>
     * Default value: true
     */
    JDConfiguration mergeEmptyLines(boolean flag);

    /**
     * Unicode escape
     * <p>
     * Default value: false
     */
    JDConfiguration unicodeEscape(boolean flag);

    /**
     * Show line numbers
     * <p>
     * Default value: false
     */
    JDConfiguration showLineNumbers(boolean flag);

    /**
     * Indentation string
     * <p>
     * Default value: 3 spaces
     */
    JDConfiguration setCountIndentSpaces(int indent);
}