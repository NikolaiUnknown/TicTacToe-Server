package com.tictactoe.server.repositories;

import org.springframework.data.repository.CrudRepository;

import com.tictactoe.server.models.Game;

public interface GameRepository extends CrudRepository<Game,Long> {
    
}
