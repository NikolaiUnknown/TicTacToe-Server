package com.tictactoe.server.exceptions;

import com.tictactoe.server.enums.GameFieldValue;

public class PrematureMoveException extends RuntimeException {
    public PrematureMoveException(GameFieldValue fieldValue){
        super("%s is moving now".formatted(fieldValue));
    }
}
