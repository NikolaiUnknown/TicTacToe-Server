package com.tictactoe.server.services;

import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.models.Game;

import java.util.List;

public interface GameService {
    
    Long createGame(Long firstPlayerId, Long secondPlayerId);

    void move(Long playerId, Long gameId, GameCoord coord);

    List<Game> getPropositions(Long userId);

    GameSession acceptProposition(Long gameId, Long playerId);

    List<Game> getAllGames(Long id);

}
