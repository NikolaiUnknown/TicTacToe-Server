package com.tictactoe.server.services;

import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;

import java.util.List;

public interface GameService {
    
    Long createGame(Long firstPlayerId, Long secondPlayerId);

    void move(Long playerId, Long gameId, GameCoord coord);

    List<Game> getPropositions(Long userId);

    void regResult(Long gameId, GameSessionStatus status);

    GameSession acceptProposition(Long gameId, Long playerId);

    List<Game> getAllGames(Long id);

    GameFieldValue getPlayerValue(Long gameId, Long playerId);

}
