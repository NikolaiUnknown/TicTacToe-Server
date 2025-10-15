package com.tictactoe.server.exceptions;

public class FieldIsAlreadyUsedException extends WebSocketGameException{
    public FieldIsAlreadyUsedException(Long playerId,String coord){
        super(playerId,"Field %s is already used".formatted(coord));
    }
}
