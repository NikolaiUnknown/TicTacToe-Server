package com.tictactoe.server.controllers;

import com.tictactoe.server.dto.messages.ErrorMessageDto;
import com.tictactoe.server.exceptions.WebSocketGameException;
import com.tictactoe.server.services.WebSocketMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Date;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalWebSocketExceptionHandler {

    private final WebSocketMessagingService webSocketMessagingService;


    @MessageExceptionHandler
    public void fieldIsAlreadyUsedHandler(WebSocketGameException e){
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage(), new Date());
        webSocketMessagingService.sendErrorMessage(errorMessageDto, e.getPlayerId());
    }



}
