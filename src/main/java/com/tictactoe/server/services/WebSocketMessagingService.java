package com.tictactoe.server.services;

import com.tictactoe.server.dto.messages.ErrorMessageDto;
import com.tictactoe.server.dto.messages.GameConnectionStatusMessageDto;
import com.tictactoe.server.dto.messages.GameSessionStatusMessageDto;
import com.tictactoe.server.dto.messages.MoveMessageDto;

public interface WebSocketMessagingService {

    void sendMoveMessage(MoveMessageDto dto, Long gameId);

    void sendGameStatusMessage(GameSessionStatusMessageDto dto, Long gameId);

    void sendConnectionStatusMessage(GameConnectionStatusMessageDto dto, Long gameId);

    void sendErrorMessage(ErrorMessageDto dto, Long playerId);
}
