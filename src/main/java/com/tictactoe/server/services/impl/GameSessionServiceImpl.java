package com.tictactoe.server.services.impl;

import com.tictactoe.server.core.DisconnectedPlayersManager;
import com.tictactoe.server.core.GameCore;
import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.core.UnstartedGamesManager;
import com.tictactoe.server.dto.messages.GameSessionStatusMessageDto;
import com.tictactoe.server.dto.messages.MoveMessageDto;
import com.tictactoe.server.enums.ConnectionStatus;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.exceptions.GameSessionNotFoundException;
import com.tictactoe.server.exceptions.NotSessionParticipantException;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.services.GameSessionService;
import com.tictactoe.server.services.MessageCacheService;
import com.tictactoe.server.services.WebSocketMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {

    private final GameCore gameCore;
    private final WebSocketMessagingService webSocketMessagingService;
    private final MessageCacheService messageCacheService;
    private final UnstartedGamesManager unstartedGamesManager;
    private final DisconnectedPlayersManager disconnectedPlayersManager;

    @Override
    public void registerGameSessionResult(Long gameId, GameSessionStatus status){
        messageCacheService.removeGameFromCache(gameId);
        deleteGameSession(gameId);
        webSocketMessagingService.sendGameStatusMessage(new GameSessionStatusMessageDto(status),gameId);
    }

    @Override
    @Transactional
    public GameSessionStatus moveInSession(Long playerId, Long gameId, GameCoord coord) {
        GameSession session = gameCore.findSessionById(gameId)
                .orElseThrow(() -> new GameSessionNotFoundException(playerId));
        if (!session.getPlayers().containsKey(playerId)) {
            throw new NotSessionParticipantException(playerId);
        }
        GameSessionStatus status = session.move(playerId, coord);
        webSocketMessagingService.sendMoveMessage(new MoveMessageDto(playerId,coord),gameId);
        return status;
    }

    public void deleteGameSession(Long gameId){
        gameCore.deleteSessionById(gameId);
    }

    @Override
    public Set<MoveMessageDto> getMoves(Long playerId,Long gameId) {
        GameSession session = gameCore.findSessionById(gameId)
                .orElseThrow(() -> new GameSessionNotFoundException(playerId));
        if (!session.getPlayers().containsKey(playerId)) {
            throw new NotSessionParticipantException(playerId);
        }
        Set<MoveMessageDto> moves = new HashSet<>();
        Long playerX = 0L;
        Long playerO = 0L;
        for (Long player: session.getPlayers().keySet()){
            if (session.getPlayers().get(player).equals(GameFieldValue.X)){
                playerX = player;
            } else {
                playerO = player;
            }
        }
        for (GameCoord coord: session.getGameBoard().keySet()){
            if (session.getGameBoard().get(coord).equals(GameFieldValue.X)){
                moves.add(new MoveMessageDto(playerX,coord));
            } else if (session.getGameBoard().get(coord).equals(GameFieldValue.O)){
                moves.add(new MoveMessageDto(playerO,coord));
            }
        }
        return moves;
    }

    @Override
    public GameFieldValue getPlayerValue(Long gameId, Long playerId){
        var gameSession = gameCore.findSessionById(gameId).orElseThrow(() -> new GameSessionNotFoundException(playerId));
        return gameSession.getPlayers().getOrDefault(playerId, GameFieldValue.NONE);
    }

    @Override
    public ConnectionStatus getPlayerConnectionStatus(Long gameId, Long playerId) {
        if (disconnectedPlayersManager.isDisconnected(gameId, playerId)){
            return ConnectionStatus.DISCONNECTED;
        }
        return ConnectionStatus.CONNECTED;
    }

    @Override
    public GameSession createGameSession(Game game) {
        unstartedGamesManager.markUnstarted(game.getId(),game.getDateOfStart().getTime());
        return gameCore.createNewGameSession(game);
    }
}
