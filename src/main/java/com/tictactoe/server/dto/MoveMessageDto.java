package com.tictactoe.server.dto;

import com.tictactoe.server.enums.GameCoord;

public record MoveMessageDto(Long playerId, GameCoord coord) {
}
