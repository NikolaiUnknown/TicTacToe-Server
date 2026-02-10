package com.tictactoe.server.dto.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerLastGameResultResponseDto {
    private Long id;
    private String nickname ;
    private Integer rating;
    private Long winnerId;
    private Date dateOfEnd;
}
