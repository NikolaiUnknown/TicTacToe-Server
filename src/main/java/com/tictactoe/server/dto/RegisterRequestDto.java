package com.tictactoe.server.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequestDto {
    @Size(min = 4, message = "Nickname length must be more than 4")
    private String nickname;

    @Size(min = 6, message = "Password length must be more than 6")
    private String password;
}
