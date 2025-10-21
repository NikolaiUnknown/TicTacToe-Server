package com.tictactoe.server.services.impl;

import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.services.GameService;
import com.tictactoe.server.services.GameSessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GameMoveServiceTest {
    @Mock
    private GameSessionService gameSessionService;
    @Mock
    private GameService gameService;

    @InjectMocks
    private GameMoveServiceImpl gameMoveService;

    @Test
    void testSuccessfulContinueMove() {
        when(gameSessionService.moveInSession(0L,0L,GameCoord.COORD_0_0))
                .thenReturn(GameSessionStatus.CONTINUE);
        assertDoesNotThrow(()-> gameMoveService.move(0L,0L, GameCoord.COORD_0_0));
    }

    @Test
    void testSuccessfulEndMove() {
        when(gameSessionService.moveInSession(0L,0L,GameCoord.COORD_0_0))
                .thenReturn(GameSessionStatus.TIE);
        assertDoesNotThrow(()-> gameMoveService.move(0L,0L, GameCoord.COORD_0_0));
        verify(gameService).registerGameResult(0L,GameSessionStatus.TIE);
    }

}