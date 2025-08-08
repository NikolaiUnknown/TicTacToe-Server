package com.tictactoe.server.services;

import org.springframework.stereotype.Service;

import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.models.Game;

@Service
public interface GameService {
    
    public void createGame(Game game);

    public GameSession createGameSession(Game game);

    public void move(Long playerId, Long gameId, GameCoord coord);

}
