package com.tictactoe.server.dto.player;

public record PlayerStatsResponseDto(int games, int wins, int loses, int ties) {
}
