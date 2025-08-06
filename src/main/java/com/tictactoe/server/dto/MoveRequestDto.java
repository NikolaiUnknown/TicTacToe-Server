package com.tictactoe.server.dto;

import com.tictactoe.server.enums.GameCoord;

public record MoveRequestDto(GameCoord coord, Long gameId) {
    
}
