package com.tictactoe.server.dto.messages;

import com.tictactoe.server.enums.ConnectionStatus;

public record GameConnectionStatusMessageDto(ConnectionStatus status, Long playerId) {
}
