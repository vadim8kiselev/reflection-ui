package com.classparser.exception.agent;

import com.classparser.exception.ByteCodeParserException;

public class InvalidAgentClassException extends ByteCodeParserException {

    public InvalidAgentClassException(String message) {
        super(message);
    }
}
