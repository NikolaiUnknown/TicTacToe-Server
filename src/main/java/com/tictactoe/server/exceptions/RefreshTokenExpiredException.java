package com.tictactoe.server.exceptions;

public class RefreshTokenExpiredException extends RuntimeException{
    public RefreshTokenExpiredException(String msg){
        super(msg);
    }
}
