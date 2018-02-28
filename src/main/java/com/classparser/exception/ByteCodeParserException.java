package com.classparser.exception;

public class ByteCodeParserException extends RuntimeException {

    public ByteCodeParserException(String message) {
        super(message);
    }

    public ByteCodeParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
