package com.kiselev.classparser.exception;

public class ReflectionParserException extends RuntimeException {

    public ReflectionParserException(String message) {
        super(message);
    }

    public ReflectionParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
