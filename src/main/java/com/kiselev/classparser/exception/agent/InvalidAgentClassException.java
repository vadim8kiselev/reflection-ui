package com.kiselev.classparser.exception.agent;

import com.kiselev.classparser.exception.ByteCodeParserException;

public class InvalidAgentClassException extends ByteCodeParserException {

    public InvalidAgentClassException(String message) {
        super(message);
    }
}
