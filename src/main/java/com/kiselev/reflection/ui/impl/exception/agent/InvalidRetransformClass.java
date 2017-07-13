package com.kiselev.reflection.ui.impl.exception.agent;

import com.kiselev.reflection.ui.impl.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 13.07.2017.
 */
public class InvalidRetransformClass extends ByteCodeParserException {

    public InvalidRetransformClass(String message) {
        super(message);
    }

    public InvalidRetransformClass(String message, Throwable cause) {
        super(message, cause);
    }
}
