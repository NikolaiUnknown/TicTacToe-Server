package com.tictactoe.server.dto.player;

public record LeaderResponseDto (PlayerResponseDto player, PlayerStatsResponseDto stats) {};