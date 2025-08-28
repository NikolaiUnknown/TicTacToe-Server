package com.tictactoe.server.dto;

import com.tictactoe.server.enums.GameSessionStatus;

public record GameSessionStatusMessageDto(GameSessionStatus status) {
}
