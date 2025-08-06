package com.tictactoe.server.exceptions;

public class FieldAlreadyUsedException extends RuntimeException{
    public FieldAlreadyUsedException(String msg){
        super(msg);
    }
}
