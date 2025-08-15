package com.tictactoe.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.models.Game;

@Repository
public interface GameRepository extends CrudRepository<Game,Long> {
    
    List<Game> findAllGamesBySecondPlayerIdAndStatus(Long id, GameStatus status);

    //FIXME
    // @Query("""
    //         SELECT g
    //         FROM Game g
    //         WHERE g.firstPlayer.id=:id
    //         OR g.secondPlayer.id=:id
    //         """)
    @Query(value = """
                   SELECT * FROM games 
                   WHERE first_player_id=?1 
                   OR second_player_id =?1
                   """, nativeQuery = true)
    List<Game> findGamesByPlayerId(Long id);
}
