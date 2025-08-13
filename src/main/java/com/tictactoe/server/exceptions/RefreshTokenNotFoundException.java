package com.tictactoe.server.exceptions;

public class RefreshTokenNotFoundException extends EntityNotFoundException{
    public RefreshTokenNotFoundException(){
        super("Refresh token not found!");
    }
}
