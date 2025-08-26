package com.tictactoe.server.controllers;

import com.tictactoe.server.dto.CreateGameRequestDto;
import com.tictactoe.server.dto.GameResponseDto;
import com.tictactoe.server.mappers.GameMapper;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;

    @GetMapping("/")
    public ResponseEntity<List<GameResponseDto>> getAllGames(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        var games = gameMapper.gamesToDtos(
            gameService.getAllGames(userDetails.getPlayer().getId())
        ); 
        return ResponseEntity.ok(games);
    }

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
    public ResponseEntity<Long> proposeGame(@RequestBody CreateGameRequestDto createGameRequestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long gameId = gameService.createGame(userDetails.getPlayer().getId(),createGameRequestDto.enemyId());
        return new ResponseEntity<>(gameId,HttpStatus.CREATED);
    }

}
