package com.tictactoe.server.exceptions;

public class PlayerNotFoundException extends EntityNotFoundException{

    public PlayerNotFoundException() {
        super("Player not found");
    }
    
}
