package com.kiselev.classparser.exception.option;

import com.kiselev.classparser.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 28.07.2017.
 */
public class OptionNotFoundException extends ByteCodeParserException {

    public OptionNotFoundException(String message) {
        super(message);
    }
}
