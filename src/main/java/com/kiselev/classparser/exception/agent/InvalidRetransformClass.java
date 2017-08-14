package com.kiselev.classparser.exception.agent;

import com.kiselev.classparser.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 07/13/2017.
 */
public class InvalidRetransformClass extends ByteCodeParserException {

    public InvalidRetransformClass(String message) {
        super(message);
    }

    public InvalidRetransformClass(String message, Throwable cause) {
        super(message, cause);
    }
}
