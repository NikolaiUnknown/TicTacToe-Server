package com.tictactoe.server.services;

import com.tictactoe.server.enums.GameCoord;

public interface GameMoveService {
    void move(Long playerId, Long gameId, GameCoord coord);
}
