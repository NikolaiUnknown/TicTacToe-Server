package com.tictactoe.server.exceptions;

public class RefreshTokenExpiredException extends RuntimeException{
    public RefreshTokenExpiredException(){
        super("Refresh token has expired, please re-login");
    }
}
