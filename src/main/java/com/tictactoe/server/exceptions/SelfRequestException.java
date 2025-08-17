package com.tictactoe.server.exceptions;

public class SelfRequestException extends RuntimeException {
    public SelfRequestException(){
        super("This is you");
    }
}
