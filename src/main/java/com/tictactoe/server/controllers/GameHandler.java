package com.tictactoe.server.controllers;

import com.tictactoe.server.dto.MoveRequestDto;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GameHandler {

    private final GameService gameService;

    @MessageMapping("/game/move")
    public void move(Authentication authentication, @Payload MoveRequestDto moveRequestDto){
        var userDetails = (UserDetailsImpl)authentication.getPrincipal();
        gameService.move(userDetails.getPlayer().getId(), moveRequestDto.gameId(),moveRequestDto.coord());
    }

}
