package com.tictactoe.server.exceptions;

public class NotSessionParticipantException extends RuntimeException {
    public NotSessionParticipantException(Long playerId){
        super("Player %d isn't in session".formatted(playerId));
    }
}
