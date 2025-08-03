package com.tictactoe.server.exceptions;

public class RefreshTokenNotFoundException extends RuntimeException{
    public RefreshTokenNotFoundException(String msg){
        super(msg);
    }
}
