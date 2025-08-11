package com.tictactoe.server.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tictactoe.server.dto.CreateGameRequestDto;
import com.tictactoe.server.dto.GameResponseDto;
import com.tictactoe.server.mappers.GameMapper;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;

    @GetMapping("/propositions")
    public ResponseEntity<List<GameResponseDto>> getPropositions(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        var games = gameMapper.gamesToDtos(
            gameService.getPropositions(userDetails.getPlayer().getId())
        );
        return ResponseEntity.ok(games);    
    }



    @PatchMapping("/confirm")
    public ResponseEntity<Void> confirmProposition(@RequestParam("game") Long gameId,
                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        gameService.acceptProposition(gameId,userDetails.getPlayer().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<String> createGameBoard(@RequestBody CreateGameRequestDto createGameRequestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        
        Long gameId = gameService.createGame(userDetails.getPlayer().getId(),createGameRequestDto.enemyId());
        return new ResponseEntity<>(String.valueOf(gameId),HttpStatus.CREATED);
    }

}
