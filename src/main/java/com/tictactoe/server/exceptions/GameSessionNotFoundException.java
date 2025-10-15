package com.tictactoe.server.exceptions;

public class GameSessionNotFoundException extends WebSocketGameException{
    public GameSessionNotFoundException(Long playerId){
        super(playerId,"Game session not found");
    }
}
