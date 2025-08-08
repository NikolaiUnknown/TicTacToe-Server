package com.tictactoe.server.exceptions;

public class NotSessionParticipantException extends RuntimeException {
    public NotSessionParticipantException(String msg){
        super(msg);
    }
}
