package com.tictactoe.server.exceptions;

import lombok.Getter;

public abstract class WebSocketGameException extends RuntimeException {
    @Getter
    private final Long playerId;

    public WebSocketGameException(Long playerId, String message) {
        super(message);
        this.playerId = playerId;
    }
}
