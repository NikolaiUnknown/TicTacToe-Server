package com.tictactoe.server.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerId;

    private String token;

    private Date expiryDate;

    public RefreshToken(Long playerId, String token, Date expiryDate) {
        this.playerId = playerId;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
