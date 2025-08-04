package com.tictactoe.server.models;

import java.util.Date;

import com.tictactoe.server.enums.GameStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "games")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="first_player_id", referencedColumnName = "id")
    private Player firstPlayer;

    @ManyToOne
    @JoinColumn(name ="second_player_id", referencedColumnName = "id")
    private Player secondPlayer;
    
    @ManyToOne
    @JoinColumn(name ="winner_id", referencedColumnName = "id")
    private Player winner;
    
    private Date dateOfStart;

    private Date dateOfEnd;

    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
}
