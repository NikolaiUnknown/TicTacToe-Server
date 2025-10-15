package com.tictactoe.server.services;

import com.tictactoe.server.dto.ErrorMessageDto;
import com.tictactoe.server.dto.GameConnectionStatusMessageDto;
import com.tictactoe.server.dto.GameSessionStatusMessageDto;
import com.tictactoe.server.dto.MoveMessageDto;

public interface WebSocketMessagingService {

    void sendMoveMessage(MoveMessageDto dto, Long gameId);

    void sendGameStatusMessage(GameSessionStatusMessageDto dto, Long gameId);

    void sendConnectionStatusMessage(GameConnectionStatusMessageDto dto, Long gameId);

    void sendErrorMessage(ErrorMessageDto dto, Long playerId);
}
