package com.classparser.exception.option;

import com.classparser.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 28.07.2017.
 */
public class OptionNotFoundException extends ByteCodeParserException {

    public OptionNotFoundException(String message) {
        super(message);
    }
}
