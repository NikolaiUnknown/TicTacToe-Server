package com.tictactoe.server.services.impl;

import com.tictactoe.server.core.DisconnectedPlayersManager;
import com.tictactoe.server.core.GameCore;
import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.core.UnstartedGamesManager;
import com.tictactoe.server.enums.*;
import com.tictactoe.server.exceptions.FieldIsAlreadyUsedException;
import com.tictactoe.server.exceptions.GameSessionNotFoundException;
import com.tictactoe.server.exceptions.NotSessionParticipantException;
import com.tictactoe.server.exceptions.PrematureMoveException;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.services.MessageCacheService;
import com.tictactoe.server.services.WebSocketMessagingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class GameSessionServiceImplTest {

    @Mock
    private GameCore gameCore;
    @Mock
    private WebSocketMessagingService webSocketMessagingService;
    @Mock
    private MessageCacheService messageCacheService;
    @Mock
    private UnstartedGamesManager unstartedGamesManager;
    @Mock
    private DisconnectedPlayersManager disconnectedPlayersManager;

    @InjectMocks
    private GameSessionServiceImpl gameSessionService;

    @Test
    void testSuccessfulRegisterGameSessionResult() {
        assertDoesNotThrow(() -> gameSessionService.registerGameSessionResult(0L,GameSessionStatus.TIE));
        verify(messageCacheService,times(1)).removeGameFromCache(0L);
        verify(gameCore,times(1)).deleteSessionById(0L);
        verify(webSocketMessagingService,times(1)).sendGameStatusMessage(any(),any(Long.class));
    }

    @Test
    void testSuccessfulDeleteGameSession() {
        assertDoesNotThrow(() -> gameSessionService.deleteGameSession(0L));
        verify(gameCore,times(1)).deleteSessionById(0L);
    }

    @Test
    void testSuccessfulGetPlayerValue() {
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(new GameSession(0L,1L)));
        assertEquals(GameFieldValue.X,gameSessionService.getPlayerValue(0L,0L));
        assertEquals(GameFieldValue.O,gameSessionService.getPlayerValue(0L,1L));
        assertEquals(GameFieldValue.NONE,gameSessionService.getPlayerValue(0L,2L));
    }

    @Test
    void testGetPlayerValueFromNonExistGame() {
        assertThrows(GameSessionNotFoundException.class,
                () -> gameSessionService.getPlayerValue(0L,0L));
    }

    @Test
    void testGetNonMemberPlayerValue() {
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(new GameSession(1L, 2L)));
        assertEquals(GameFieldValue.NONE, gameSessionService.getPlayerValue(0L,0L));
    }

    @Test
    void testSuccessfulCreateGameSession() {
        Game game = Game.builder()
                .id(0L)
                .dateOfStart(new Date())
                .build();
        var gameSession = new GameSession(0L,1L);
        when(gameCore.createNewGameSession(game)).thenReturn(gameSession);
        assertEquals(gameSession,gameSessionService.createGameSession(game));
        verify(unstartedGamesManager,times(1)).markUnstarted(0L,game.getDateOfStart().getTime());
    }

    @Test
    void testWinMove() {
        GameSession session = new GameSession(0L,1L);
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(session));
        session.move(0L, GameCoord.COORD_0_0);
        session.move(1L,GameCoord.COORD_1_0);
        session.move(0L,GameCoord.COORD_0_1);
        session.move(1L,GameCoord.COORD_1_1);
        assertEquals(GameSessionStatus.X_WIN, gameSessionService.moveInSession(0L,0L,GameCoord.COORD_0_2));
    }

    @Test
    void testPrematureMove() {
        GameSession session = new GameSession(0L,1L);
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(session));
        session.move(0L,GameCoord.COORD_0_0);
        assertThrows(PrematureMoveException.class,
                ()-> gameSessionService.moveInSession(0L,0L,GameCoord.COORD_0_1));
    }


    @Test
    void testMoveInAlreadyUsedField() {
        GameSession session = new GameSession(0L,1L);
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(session));
        session.move(0L,GameCoord.COORD_0_0);
        assertThrows(FieldIsAlreadyUsedException.class,
                ()-> gameSessionService.moveInSession(1L,0L,GameCoord.COORD_0_0));

    }

    @Test
    void testMoveInForbiddenGame() {
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(new GameSession(1L,2L)));
        assertThrows(NotSessionParticipantException.class,
                ()-> gameSessionService.moveInSession(0L, 0L, null));
    }

    @Test
    void testMoveWithNonExistGameSession() {
        assertThrows(GameSessionNotFoundException.class,
                ()-> gameSessionService.moveInSession(0L, 0L, null));
    }

    @Test
    void testSuccessfulGetPlayerConnectionStatus() {
        when(disconnectedPlayersManager.isDisconnected(0L,0L)).thenReturn(true);
        assertEquals(ConnectionStatus.DISCONNECTED, gameSessionService.getPlayerConnectionStatus(0L,0L));
    }

}