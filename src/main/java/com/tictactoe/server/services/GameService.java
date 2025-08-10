package com.tictactoe.server.services;

import org.springframework.stereotype.Service;

import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameCoord;

@Service
public interface GameService {
    
    public void createGame(Long firstPlayerId, Long secondPlayerId);


    public void move(Long playerId, Long gameId, GameCoord coord);

    public GameSession acceptProposition(Long gameId, Long playerId);

}
