package com.tictactoe.server.controllers;

import com.tictactoe.server.dto.MoveRequestDto;
import com.tictactoe.server.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GameHandler {

    private final GameService gameService;

    @MessageMapping("/game/move")
    public void move(Principal principal, @Payload MoveRequestDto moveRequestDto){
        System.out.println("Get message " + moveRequestDto);
        gameService.move(Long.parseLong(principal.getName()),moveRequestDto.gameId(),moveRequestDto.coord());
    }

}
