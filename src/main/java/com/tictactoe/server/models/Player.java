package com.tictactoe.server.models;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="players")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nickname;
    
    private String password;

    private Date dateOfRegistration;

    private Integer rating;

    public Player(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "firstPlayer")
    private List<Game> proposedGames;

    @OneToMany(mappedBy = "secondPlayer")
    private List<Game> receivedGames;

    @OneToMany(mappedBy = "winner")
    private List<Game> wonGames;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(nickname, player.nickname) && Objects.equals(password, player.password) && Objects.equals(dateOfRegistration, player.dateOfRegistration) && Objects.equals(rating, player.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, password, dateOfRegistration, rating);
    }
}
