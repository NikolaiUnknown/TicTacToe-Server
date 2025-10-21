package com.tictactoe.server.services.impl;

import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.services.GameMoveService;
import com.tictactoe.server.services.GameService;
import com.tictactoe.server.services.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameMoveServiceImpl implements GameMoveService {

    private final GameSessionService gameSessionService;
    private final GameService gameService;

    @Override
    @Transactional
    public void move(Long playerId, Long gameId, GameCoord coord) {
        GameSessionStatus status = gameSessionService.moveInSession(playerId,gameId,coord);
        if (!status.equals(GameSessionStatus.CONTINUE)) {
            gameService.registerGameResult(gameId,status);
        }
    }
}
