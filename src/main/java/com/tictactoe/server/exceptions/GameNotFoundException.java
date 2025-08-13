package com.tictactoe.server.exceptions;

public class GameNotFoundException extends EntityNotFoundException{
    public GameNotFoundException(){
        super("Game not found");
    }
}
