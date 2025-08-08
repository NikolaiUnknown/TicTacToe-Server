package com.tictactoe.server.exceptions;

public class GameSessionNotFoundException extends RuntimeException{
    public GameSessionNotFoundException(String msg){
        super(msg);
    }
}
