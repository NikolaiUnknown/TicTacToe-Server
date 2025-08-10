package com.tictactoe.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tictactoe.server.dto.CreateGameRequestDto;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;



    @PatchMapping("/confirm")
    public ResponseEntity<Void> confirmProposition(@RequestParam("game") Long gameId,
                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        gameService.acceptProposition(gameId,userDetails.getPlayer().getId());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createGameBoard(@RequestBody CreateGameRequestDto createGameRequestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        
        gameService.createGame(userDetails.getPlayer().getId(),createGameRequestDto.enemyId());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
