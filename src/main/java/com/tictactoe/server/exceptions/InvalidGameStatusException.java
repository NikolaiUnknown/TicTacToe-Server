package com.tictactoe.server.exceptions;

import com.tictactoe.server.enums.GameStatus;

public class InvalidGameStatusException extends RuntimeException{
    
    public InvalidGameStatusException(GameStatus status){
        super(switch (status) {
            case GameStatus.IN_PROCESS -> "This game is already started";
            case GameStatus.CANCELED -> "This game is canceled";
            case GameStatus.COMPLETED -> "This game is completed";
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        });
        
    }
}
