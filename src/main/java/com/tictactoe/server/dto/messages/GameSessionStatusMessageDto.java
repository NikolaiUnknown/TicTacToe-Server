package com.tictactoe.server.dto.messages;

import com.tictactoe.server.enums.GameSessionStatus;

public record GameSessionStatusMessageDto(GameSessionStatus status) {
}
