package com.tictactoe.server.dto;

import java.util.Date;

public record ErrorMessageDto(String errorMsg, Date date) {
}
