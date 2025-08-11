package com.tictactoe.server.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.models.Game;

@Repository
public interface GameRepository extends CrudRepository<Game,Long> {
    
    List<Game> findAllGamesBySecondPlayerIdAndStatus(Long id, GameStatus status);
}
