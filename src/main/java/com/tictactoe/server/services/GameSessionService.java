package com.tictactoe.server.services;

import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.dto.messages.MoveMessageDto;
import com.tictactoe.server.enums.ConnectionStatus;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.models.Game;

import java.util.Set;

public interface GameSessionService {

    GameSessionStatus moveInSession(Long playerId, Long gameId, GameCoord coord);
    void registerGameSessionResult(Long gameId, GameSessionStatus status);
    GameFieldValue getPlayerValue(Long gameId, Long playerId);
    ConnectionStatus getPlayerConnectionStatus(Long gameId, Long playerId);
    GameSession createGameSession(Game game);
    void deleteGameSession(Long gameId);
    Set<MoveMessageDto> getMoves(Long playerId,Long gameId);
}
