package com.tictactoe.server.dto;

public record JwtResponseDto(String accessToken, String refreshToken) {
}