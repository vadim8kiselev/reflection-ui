package com.kiselev.reflection.ui.impl.exception;

/**
 * Created by Aleksei Makarov on 13.07.2017.
 */
public class ReflectionParserException extends RuntimeException {

    public ReflectionParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionParserException(Throwable cause) {
        super(cause);
    }
}
