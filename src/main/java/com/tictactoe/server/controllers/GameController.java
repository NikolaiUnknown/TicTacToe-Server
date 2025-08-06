package com.tictactoe.server.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tictactoe.server.core.GameCore;
import com.tictactoe.server.dto.MoveRequestDto;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameCore gameCore;
    private final EntityManager entityManager;

    //TODO
    @GetMapping("/")
    public String logGames(){
        return gameCore.getGames().toString();
    }

    @RequestMapping(value = "/move", method = RequestMethod.PATCH)
    public ResponseEntity<Void> move(@RequestBody MoveRequestDto requestDto,
                     @RequestParam("playerId") Long playerId){
        gameCore.move(playerId, requestDto.gameId(), requestDto.coord());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/print/{id}")
    public String  pringGameBoard(@PathVariable("id") Long id){
        return gameCore.getGames().get(id).printGameBorder();
    }

    @PostMapping("/")
    @Transactional
    public void createGameBoard(){
        Player player1 = entityManager.find(Player.class,3);
        Player player2 = entityManager.find(Player.class,4);
        Game game = Game.builder()
                        .firstPlayer(player1)
                        .secondPlayer(player2)
                        .dateOfStart(new Date())
                        .status(GameStatus.IN_PROCESS)
                        .build();
        entityManager.persist(game);
        System.out.println(game.getId());
        gameCore.createNewGameSession(game);;
    }



    //TODO
}
