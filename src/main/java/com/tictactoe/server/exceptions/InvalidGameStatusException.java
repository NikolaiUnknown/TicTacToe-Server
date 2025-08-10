package com.tictactoe.server.exceptions;

import com.tictactoe.server.enums.GameStatus;

import lombok.Getter;

public class InvalidGameStatusException extends RuntimeException{

    @Getter
    private final GameStatus status;
    
    public InvalidGameStatusException(GameStatus status){
        this.status = status;
    }
}
