package com.tictactoe.server.dto;

public record PlayerStatsResponseDto(int games, int wins, int loses, int ties) {
}
