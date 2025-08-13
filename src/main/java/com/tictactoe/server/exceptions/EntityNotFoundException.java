package com.tictactoe.server.exceptions;

public abstract class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String msg){
        super(msg);
    }
}
