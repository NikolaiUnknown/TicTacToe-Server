package com.tictactoe.server.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.models.Game;

import lombok.Getter;

@Component
public class GameCore {
    @Getter
    private final Map<Long,GameSession> games;

    public GameCore() {
        this.games = new ConcurrentHashMap<>();
    }


    public void createNewGameSession(Game game){
        GameSession session = new GameSession(game.getFirstPlayer().getId(),game.getSecondPlayer().getId());
        games.put(game.getId(), session);
    }


    public void move(Long playerId, Long gameId, GameCoord coord){
        GameSession session = games.get(gameId);
        GameSessionStatus status = session.move(playerId, coord);
        switch(status){
            case GameSessionStatus.TIE -> {
                //Game registration logic
                System.out.println("TIE");

            }
            case GameSessionStatus.X_WIN -> {
                //Game registration logic
                System.out.println("X WINS");
            }
            case GameSessionStatus.O_WIN -> {
                //Game registration logic
                System.out.println("O WINS");
            }
            default -> {
                return;
            }
        }
        games.remove(gameId);
    }
    
}
