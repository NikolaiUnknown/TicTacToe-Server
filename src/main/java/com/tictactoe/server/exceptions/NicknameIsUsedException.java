package com.tictactoe.server.exceptions;

public class NicknameIsUsedException extends RuntimeException {

    public NicknameIsUsedException(String nickname) {
        super("Nickname %s is already use!".formatted(nickname));
    }
    
}
