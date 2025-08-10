package com.tictactoe.server.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tictactoe.server.dto.CreateGameRequestDto;
import com.tictactoe.server.dto.MoveRequestDto;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.GameService;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    

    @PostMapping("/")
    @Transactional
    public ResponseEntity<Void> createGameBoard(@RequestBody CreateGameRequestDto createGameRequestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        Game game = Game.builder()
                        .firstPlayer(userDetails.getPlayer())
                        .secondPlayer(new Player(createGameRequestDto.enemyId()))
                        .dateOfStart(new Date())
                        .status(GameStatus.PROPOSED)
                        .build();
        gameService.createGame(game);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
