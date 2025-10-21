package com.tictactoe.server.services.impl;

import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.exceptions.GameNotFoundException;
import com.tictactoe.server.exceptions.InvalidGameStatusException;
import com.tictactoe.server.exceptions.PlayerNotFoundException;
import com.tictactoe.server.exceptions.SelfRequestException;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.repositories.GameRepository;
import com.tictactoe.server.repositories.PlayerRepository;
import com.tictactoe.server.services.GameSessionService;
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
    private GameSessionService gameSessionService;
    @Mock
    private PlayerRepository playerRepository;


    @InjectMocks
    private GameServiceImpl gameServiceImpl;


    @Test
    void testAcceptSuccessfulProposition() {
        Game game = Game.builder()
                    .secondPlayer(new Player(0L))
                    .dateOfStart(new Date())
                    .status(GameStatus.PROPOSED)
                    .build();
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        var session = new GameSession(0L,1L);
        when(gameSessionService.createGameSession(game)).thenReturn(session);
        assertEquals(session,gameServiceImpl.acceptProposition(0L,0L));
        assertEquals(GameStatus.IN_PROCESS, game.getStatus());
        verify(gameRepository,times(1)).save(any(Game.class));
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
    void testSuccessfulRegisterGameResult() {
        Game game = Game.builder()
                .id(0L)
                .firstPlayer(new Player(0L))
                .secondPlayer(new Player(1L))
                .build();
        game.getFirstPlayer().setRating(0);
        game.getSecondPlayer().setRating(0);
        when(gameRepository.findById(0L)).thenReturn(Optional.of(game));
        assertDoesNotThrow(() -> gameServiceImpl.registerGameResult(0L,GameSessionStatus.X_WIN));
        verify(gameRepository,times(1)).save(any(Game.class));
        verify(gameSessionService, times(1)).registerGameSessionResult(0L,GameSessionStatus.X_WIN);
        verify(playerRepository,times(1)).saveAll(anyCollection());
    }

    @Test
    void testRegisterNonExistGameResult() {
        assertThrows(GameNotFoundException.class,
                ()-> gameServiceImpl.registerGameResult(0L, GameSessionStatus.X_WIN));
    }
}
