package com.tictactoe.server.exceptions;

import org.springframework.validation.BindingResult;

import lombok.Getter;

public class InvalidRequestBodyException extends RuntimeException {
    @Getter
    private final BindingResult bindingResult;
    public InvalidRequestBodyException(BindingResult bindingResult){
        this.bindingResult = bindingResult;
    }
}
