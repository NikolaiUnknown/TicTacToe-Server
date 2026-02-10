package com.tictactoe.server.dto.messages;

import java.util.Date;

public record ErrorMessageDto(String errorMsg, Date date) {
}
