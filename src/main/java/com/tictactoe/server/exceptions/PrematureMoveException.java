package com.tictactoe.server.exceptions;

import com.tictactoe.server.enums.GameFieldValue;

public class PrematureMoveException extends WebSocketGameException {
    public PrematureMoveException(Long playerId,GameFieldValue fieldValue){
        super(playerId,"%s is moving now".formatted(fieldValue));
    }
}
