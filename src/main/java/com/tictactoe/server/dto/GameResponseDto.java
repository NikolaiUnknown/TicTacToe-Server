package com.tictactoe.server.dto;

import java.util.Date;

import com.tictactoe.server.enums.GameStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResponseDto {
    private Long id;
    private Long firstPlayerId;
    private Long secondPlayerId;
    private Long winnerId;
    private Date dateOfStart;
    private Date dateOfEnd;
    private GameStatus status;
    
}
