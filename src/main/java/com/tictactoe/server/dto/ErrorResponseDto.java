package com.tictactoe.server.dto;

import java.util.Date;

public record ErrorResponseDto(String errorMsg, Date date, Integer statusCode) {
}
