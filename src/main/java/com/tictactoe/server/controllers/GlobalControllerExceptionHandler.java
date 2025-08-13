package com.tictactoe.server.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import com.tictactoe.server.dto.ErrorResponseDto;
import com.tictactoe.server.exceptions.EntityNotFoundException;
import com.tictactoe.server.exceptions.InvalidRequestBodyException;
import com.tictactoe.server.exceptions.NicknameIsUsedException;
import com.tictactoe.server.exceptions.NotSessionParticipantException;
import com.tictactoe.server.exceptions.RefreshTokenExpiredException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    
    @ExceptionHandler(exception = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> usernameNotFoundHandler(EntityNotFoundException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(exception = NicknameIsUsedException.class)
    public ResponseEntity<ErrorResponseDto> nicknameIsUsedHandler(NicknameIsUsedException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),400);
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> badCredentialsHandler(BadCredentialsException e){
        ErrorResponseDto error = new ErrorResponseDto("Nickname or password is incorrect!",new Date(),401);
        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(exception = InvalidRequestBodyException.class)
    public ResponseEntity<ErrorResponseDto> invalidRequestHandler(InvalidRequestBodyException e){
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMsg.append(error.getDefaultMessage());
                errorMsg.append(";\n");
            }
        ErrorResponseDto error = new ErrorResponseDto(errorMsg.toString(),new Date(),400);
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    } 


    @ExceptionHandler(exception = RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponseDto> refreshTokenExpiredHandler(RefreshTokenExpiredException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> gameForbiddenHandler(AccessDeniedException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),403);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(exception = NotSessionParticipantException.class)
    public ResponseEntity<ErrorResponseDto> gameSessionForbiddenHandler(NotSessionParticipantException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),403);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
 }
