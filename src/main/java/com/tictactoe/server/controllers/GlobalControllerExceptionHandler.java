package com.tictactoe.server.controllers;

import com.tictactoe.server.dto.ErrorResponseDto;
import com.tictactoe.server.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

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

    @ExceptionHandler(exception = FieldIsAlreadyUsedException.class)
    public ResponseEntity<ErrorResponseDto> fieldExceptionHandler(FieldIsAlreadyUsedException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = PrematureMoveException.class)
    public ResponseEntity<ErrorResponseDto> prematureMoveHandler(PrematureMoveException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = SelfRequestException.class)
    public ResponseEntity<ErrorResponseDto> selfRequestHandler(SelfRequestException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = InvalidGameStatusException.class)
    public ResponseEntity<ErrorResponseDto> invalidGameStatusHandler(InvalidGameStatusException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = GameSessionNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> gameSessionNotFoundHandler(GameSessionNotFoundException e){
        ErrorResponseDto error = new ErrorResponseDto(e.getMessage(),new Date(),404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


 }
