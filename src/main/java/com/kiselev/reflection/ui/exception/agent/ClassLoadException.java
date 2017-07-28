package com.kiselev.reflection.ui.exception.agent;

import com.kiselev.reflection.ui.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 07/28/2017.
 */
public class ClassLoadException extends ByteCodeParserException {

    public ClassLoadException(String message) {
        super(message);
    }

    public ClassLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
