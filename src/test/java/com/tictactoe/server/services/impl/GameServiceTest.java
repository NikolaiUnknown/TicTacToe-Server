package com.tictactoe.server.services.impl;

import com.tictactoe.server.core.GameCore;
import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.exceptions.*;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.repositories.GameRepository;
import com.tictactoe.server.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameCore gameCore;
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private WebSocketMessagingServiceImpl webSocketMessagingService;
    @Mock
    private MessageCacheServiceImpl messageCacheService;

    @InjectMocks
    private GameServiceImpl gameServiceImpl;


    @Test
    void testAcceptSuccessfulProposition() {
        Game game = Game.builder()
                    .secondPlayer(new Player(0L))
                    .status(GameStatus.PROPOSED)
                    .build();
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game)); 
        assertDoesNotThrow(() -> gameServiceImpl.acceptProposition(0L,0L));
        assertEquals(GameStatus.IN_PROCESS, game.getStatus());
        verify(gameRepository,times(1)).save(any(Game.class));
        verify(gameCore,times(1)).createNewGameSession(any(Game.class));
    }

    @Test
    void testAcceptInvalidProposition() {
        Game game = new Game();
        game.setSecondPlayer(new Player(0L));
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        game.setStatus(GameStatus.IN_PROCESS);
        assertThrows(InvalidGameStatusException.class,
                ()-> gameServiceImpl.acceptProposition(0L, 0L));
        game.setStatus(GameStatus.CANCELED);
        assertThrows(InvalidGameStatusException.class,
                ()-> gameServiceImpl.acceptProposition(0L, 0L));
        game.setStatus(GameStatus.COMPLETED);
        assertThrows(InvalidGameStatusException.class,
                ()-> gameServiceImpl.acceptProposition(0L, 0L));

    }

    @Test
    void testAcceptForbiddenProposition() {
        Game game = new Game();
        game.setSecondPlayer(new Player(1L));
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        assertThrows(AccessDeniedException.class,
                ()-> gameServiceImpl.acceptProposition(0L, 0L));
    }

    @Test
    void testAcceptNonExistProposition() {
        assertThrows(GameNotFoundException.class,
                () -> gameServiceImpl.acceptProposition(0L, 0L));
    }


    @Test
    void testSuccessfulCreateGame() {
        when(playerRepository.findById(0L)).thenReturn(Optional.of(new Player(0L)));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(new Player(1L)));

        assertDoesNotThrow(()->gameServiceImpl.createGame(0L,1L));
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void testCreateGameWithNonExistEnemy() {
        when(playerRepository.findById(0L)).thenReturn(Optional.of(new Player(0L)));
        assertThrows(PlayerNotFoundException.class,
                ()->gameServiceImpl.createGame(0L,1L));
    }

    @Test
    void testCreateGameWithYourself() {
        assertThrows(SelfRequestException.class,
                ()->gameServiceImpl.createGame(0L,0L));
    }

    @Test
    void testSuccessfulContinueMove() {
        GameSession session = new GameSession(0L,1L);
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(session));
        assertDoesNotThrow(()-> gameServiceImpl.move(0L,0L,GameCoord.COORD_0_0));
    }

    @Test
    void testWinMove() {
        GameSession session = new GameSession(0L,1L);
        Player playerX = new Player(0L);
        Player playerO = new Player(1L);
        playerX.setRating(0);
        playerO.setRating(0);
        Game game = Game.builder()
                    .id(0L)
                    .firstPlayer(playerX)
                    .secondPlayer(playerO)
                    .dateOfStart(new Date())
                    .status(GameStatus.IN_PROCESS)
                    .build();
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(session));
        when(gameRepository.findById(0L)).thenReturn(Optional.of(game));
        session.move(0L,GameCoord.COORD_0_0);
        session.move(1L,GameCoord.COORD_1_0);
        session.move(0L,GameCoord.COORD_0_1);
        session.move(1L,GameCoord.COORD_1_1);
        assertDoesNotThrow(()-> gameServiceImpl.move(0L,0L,GameCoord.COORD_0_2));
        verify(gameRepository,times(1)).save(any(Game.class));
    }

    @Test
    void testPrematureMove() {
        GameSession session = new GameSession(0L,1L);
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(session));
        session.move(0L,GameCoord.COORD_0_0);
        assertThrows(PrematureMoveException.class,
                ()-> gameServiceImpl.move(0L,0L,GameCoord.COORD_0_1));
    }

    
    @Test
    void testMoveInAlreadyUsedField() {
        GameSession session = new GameSession(0L,1L);
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(session));
        session.move(0L,GameCoord.COORD_0_0);
        assertThrows(FieldIsAlreadyUsedException.class,
                ()-> gameServiceImpl.move(1L,0L,GameCoord.COORD_0_0));

    }

    @Test
    void testMoveInForbiddenGame() {
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(new GameSession(1L,2L)));
        assertThrows(NotSessionParticipantException.class,
                ()-> gameServiceImpl.move(0L, 0L, null));
    }

    @Test
    void testMoveWithNonExistGameSession() {
        assertThrows(GameSessionNotFoundException.class,
                ()-> gameServiceImpl.move(0L, 0L, null));
    }

    @Test
    void testGetAllGames() {
        when(gameRepository.findGamesByPlayerId(0L)).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(),gameServiceImpl.getAllGames(0L));
        verify(gameRepository,times(1)).findGamesByPlayerId(anyLong());
    }

    @Test
    void testGetPropositions() {
        when(gameRepository.findAllGamesBySecondPlayerIdAndStatus(0L, GameStatus.PROPOSED)).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(),gameServiceImpl.getPropositions(0L));
        verify(gameRepository,times(1)).findAllGamesBySecondPlayerIdAndStatus(anyLong(),any());

    }

    @Test
    void testGetPlayersValues() {
        when(gameCore.findSessionById(0L)).thenReturn(Optional.of(new GameSession(0L,1L)));
        assertEquals(GameFieldValue.X,gameServiceImpl.getPlayerValue(0L,0L));
        assertEquals(GameFieldValue.O,gameServiceImpl.getPlayerValue(0L,1L));
        assertEquals(GameFieldValue.NONE,gameServiceImpl.getPlayerValue(0L,2L));
    }

    @Test
    void testGetPlayerValueFromNonExistGame() {
        assertThrows(GameSessionNotFoundException.class,() -> gameServiceImpl.getPlayerValue(0L,0L));
    }

}
