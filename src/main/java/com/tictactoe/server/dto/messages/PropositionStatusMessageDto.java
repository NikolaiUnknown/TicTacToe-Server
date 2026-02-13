package com.tictactoe.server.dto.messages;

import com.tictactoe.server.enums.PropositionStatus;

public record PropositionStatusMessageDto(Long gameId, PropositionStatus status) {
}
