package com.kiselev.reflection.ui.impl.exception.agent;

import com.kiselev.reflection.ui.impl.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 13.07.2017.
 */
public class InvalidAgentClassException extends ByteCodeParserException {

    public InvalidAgentClassException(String message) {
        super(message);
    }
}
