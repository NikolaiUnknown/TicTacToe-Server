package com.tictactoe.server.services;

import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.models.Game;

import java.util.List;

public interface GameService {
    
    Long createGame(Long firstPlayerId, Long secondPlayerId);

    List<Game> getPropositions(Long userId);

    void registerGameResult(Long gameId, GameSessionStatus status);

    GameSession acceptProposition(Long gameId, Long playerId);

    List<Game> getAllGames(Long id);

    void cancelGame(Long gameId);

    List<Game> getGamesHistory(Long userId);
}
