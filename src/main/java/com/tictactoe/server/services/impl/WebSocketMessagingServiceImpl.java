package com.tictactoe.server.services.impl;

import com.tictactoe.server.dto.GameConnectionStatusMessageDto;
import com.tictactoe.server.dto.GameSessionStatusMessageDto;
import com.tictactoe.server.dto.MoveMessageDto;
import com.tictactoe.server.services.WebSocketMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketMessagingServiceImpl implements WebSocketMessagingService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final String GAME_MOVE_DESTINATION = "/game/moves/%s";
    private final String GAME_STATUS_DESTINATION = "/game/status/%s";
    private final String CONNECTION_STATUS_DESTINATION = "/game/connection/%s";

    @Override
    public void sendMoveMessage(MoveMessageDto dto, Long gameId) {
        simpMessagingTemplate.convertAndSend(GAME_MOVE_DESTINATION.formatted(gameId), dto);
    }

    @Override
    public void sendGameStatusMessage(GameSessionStatusMessageDto dto, Long gameId) {
        simpMessagingTemplate.convertAndSend(GAME_STATUS_DESTINATION.formatted(gameId), dto);
    }

    @Override
    public void sendConnectionStatusMessage(GameConnectionStatusMessageDto dto, Long gameId) {
        simpMessagingTemplate.convertAndSend(CONNECTION_STATUS_DESTINATION.formatted(gameId), dto);
    }
}
