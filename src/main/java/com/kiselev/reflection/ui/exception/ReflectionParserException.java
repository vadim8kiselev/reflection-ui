package com.kiselev.reflection.ui.exception;

/**
 * Created by Aleksei Makarov on 07/13/2017.
 */
public class ReflectionParserException extends RuntimeException {

    public ReflectionParserException(String message) {
        super(message);
    }

    public ReflectionParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
