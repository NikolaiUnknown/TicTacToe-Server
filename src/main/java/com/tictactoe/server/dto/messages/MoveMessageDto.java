package com.tictactoe.server.dto.messages;

import com.tictactoe.server.enums.GameCoord;

public record MoveMessageDto(Long playerId, GameCoord coord) {
}
