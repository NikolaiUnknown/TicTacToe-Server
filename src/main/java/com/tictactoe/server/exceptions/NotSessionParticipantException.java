package com.tictactoe.server.exceptions;

public class NotSessionParticipantException extends WebSocketGameException {
    public NotSessionParticipantException(Long playerId){
        super(playerId,"Player %d isn't in session".formatted(playerId));
    }
}
