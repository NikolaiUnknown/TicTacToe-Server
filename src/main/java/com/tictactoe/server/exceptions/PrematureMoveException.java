package com.tictactoe.server.exceptions;

public class PrematureMoveException extends RuntimeException {
    public PrematureMoveException(String msg){
        super(msg);
    }
}
