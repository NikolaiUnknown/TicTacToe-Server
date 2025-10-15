package com.tictactoe.server.controllers;

import com.tictactoe.server.dto.ErrorMessageDto;
import com.tictactoe.server.exceptions.FieldIsAlreadyUsedException;
import com.tictactoe.server.exceptions.WebSocketGameException;
import com.tictactoe.server.services.WebSocketMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalWebSocketExceptionHandler {

    private final WebSocketMessagingService webSocketMessagingService;


    @MessageExceptionHandler
    public void fieldIsAlreadyUsedHandler(WebSocketGameException e){
        System.out.println("handleException");
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage(), new Date());
        webSocketMessagingService.sendErrorMessage(errorMessageDto, e.getPlayerId());
    }



}
