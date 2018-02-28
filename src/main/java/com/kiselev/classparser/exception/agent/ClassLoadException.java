package com.kiselev.classparser.exception.agent;

import com.kiselev.classparser.exception.ByteCodeParserException;

public class ClassLoadException extends ByteCodeParserException {

    public ClassLoadException(String message) {
        super(message);
    }

    public ClassLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
