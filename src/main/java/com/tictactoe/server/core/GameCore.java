package com.tictactoe.server.core;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.tictactoe.server.models.Game;

@Component
public class GameCore {

    private final Map<Long,GameSession> games;

    public GameCore() {
        this.games = new ConcurrentHashMap<>();
    }

    public GameSession createNewGameSession(Game game){
        GameSession session = new GameSession(game.getFirstPlayer().getId(),game.getSecondPlayer().getId());
        games.put(game.getId(), session);
        return session;
    }

    public Optional<GameSession> findSessionById(Long gameId){
        return Optional.ofNullable(games.get(gameId));
    }

    public void deleteSessionById(Long gameId){
        games.remove(gameId);
    }
    
}
