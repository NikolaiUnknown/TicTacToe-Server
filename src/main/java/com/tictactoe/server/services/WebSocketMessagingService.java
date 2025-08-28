package com.tictactoe.server.services;

import com.tictactoe.server.dto.GameSessionStatusMessageDto;
import com.tictactoe.server.dto.MoveMessageDto;

public interface WebSocketMessagingService {

    void sendMoveMessage(MoveMessageDto dto, Long gameId);

    void sendStatusMessage(GameSessionStatusMessageDto dto, Long gameId);

}
