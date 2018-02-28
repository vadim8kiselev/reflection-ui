package com.classparser.exception.agent;

import com.classparser.exception.ByteCodeParserException;

public class ClassLoadException extends ByteCodeParserException {

    public ClassLoadException(String message) {
        super(message);
    }

    public ClassLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
