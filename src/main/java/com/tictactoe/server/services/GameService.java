package com.tictactoe.server.services;

import java.util.List;

import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.models.Game;

public interface GameService {
    
    public Long createGame(Long firstPlayerId, Long secondPlayerId);

    public void move(Long playerId, Long gameId, GameCoord coord);

    public List<Game> getPropositions(Long userId);

    public GameSession acceptProposition(Long gameId, Long playerId);

}
