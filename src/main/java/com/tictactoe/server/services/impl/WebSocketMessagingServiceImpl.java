package com.tictactoe.server.services.impl;

import com.tictactoe.server.dto.GameResponseDto;
import com.tictactoe.server.dto.messages.*;
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
    private final String PLAYER_ERROR_DESTINATION = "/errors/%s";
    private final String PLAYER_PROPOSITIONS_DESTINATION = "/players/propositions/%s";
    private final String PLAYER_PROPOSITION_STATUS_DESTINATION = "/players/status/%s";

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

    @Override
    public void sendErrorMessage(ErrorMessageDto dto, Long playerId) {
        simpMessagingTemplate.convertAndSend(PLAYER_ERROR_DESTINATION.formatted(playerId), dto);
    }

    @Override
    public void sendPropositionMessage(PropositionMessageDto dto, Long playerId) {
        simpMessagingTemplate.convertAndSend(PLAYER_PROPOSITIONS_DESTINATION.formatted(playerId), dto);
    }

    @Override
    public void sendPropositionStatusMessage(PropositionStatusMessageDto dto, Long playerId) {
        simpMessagingTemplate.convertAndSend(PLAYER_PROPOSITION_STATUS_DESTINATION.formatted(playerId), dto);
    }

}
