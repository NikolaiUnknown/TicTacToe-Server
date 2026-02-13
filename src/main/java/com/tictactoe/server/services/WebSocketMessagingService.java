package com.tictactoe.server.services;

import com.tictactoe.server.dto.GameResponseDto;
import com.tictactoe.server.dto.messages.*;

public interface WebSocketMessagingService {

    void sendMoveMessage(MoveMessageDto dto, Long gameId);

    void sendGameStatusMessage(GameSessionStatusMessageDto dto, Long gameId);

    void sendConnectionStatusMessage(GameConnectionStatusMessageDto dto, Long gameId);

    void sendErrorMessage(ErrorMessageDto dto, Long playerId);

    void sendPropositionMessage(PropositionMessageDto dto, Long playerId);

    void sendPropositionStatusMessage(PropositionStatusMessageDto dto, Long playerId);
}
