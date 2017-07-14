package com.kiselev.reflection.ui.exception.agent;

import com.kiselev.reflection.ui.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 07/13/2017.
 */
public class InvalidAgentClassException extends ByteCodeParserException {

    public InvalidAgentClassException(String message) {
        super(message);
    }
}
