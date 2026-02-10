package com.tictactoe.server.dto.player;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.tictactoe.server.models.Player}
 */
public record PlayerResponseDto(Long id, String nickname, Date dateOfRegistration,
                                Integer rating) implements Serializable{
}