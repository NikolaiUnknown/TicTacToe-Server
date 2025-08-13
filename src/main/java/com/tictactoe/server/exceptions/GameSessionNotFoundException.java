package com.tictactoe.server.exceptions;

public class GameSessionNotFoundException extends EntityNotFoundException{
    public GameSessionNotFoundException(){
        super("Game session not found");
    }
}
