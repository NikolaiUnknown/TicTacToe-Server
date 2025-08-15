package com.tictactoe.server.exceptions;

public class FieldIsAlreadyUsedException extends RuntimeException{
    public FieldIsAlreadyUsedException(String coord){
        super("Field %s is already used".formatted(coord));
    }
}
