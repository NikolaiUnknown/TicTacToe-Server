package com.tictactoe.server.exceptions;

public class NicknameIsUsedException extends RuntimeException {

    public NicknameIsUsedException(String msg) {
        super(msg);
    }
    
}
