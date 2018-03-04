package com.classparser.exception.option;

import com.classparser.exception.ByteCodeParserException;

public class OptionNotFoundException extends ByteCodeParserException {

    public OptionNotFoundException(String message) {
        super(message);
    }
}
