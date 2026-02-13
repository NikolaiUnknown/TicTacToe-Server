package com.tictactoe.server.repositories;

import java.util.List;

import com.tictactoe.server.dto.GameResponseDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.models.Game;

@Repository
public interface GameRepository extends CrudRepository<Game,Long> {

    List<Game> findAllGamesBySecondPlayerIdAndStatusOrderByDateOfStartDesc(Long secondPlayerId, GameStatus status);

    List<Game> findAllGamesByFirstPlayerIdAndStatusOrderByDateOfStartDesc(Long id, GameStatus status);


    @Query("""
            SELECT g
            FROM Game g
            WHERE g.firstPlayer.id =:id
            OR g.secondPlayer.id =:id
            """)
    List<Game> findGamesByPlayerId(@Param("id") Long id);

    @Query("""
            SELECT g
            FROM Game g
            WHERE (g.firstPlayer.id =:id
            OR g.secondPlayer.id =:id)
            AND g.status= :status
            ORDER BY g.dateOfStart DESC
            """)
    List<Game> findGamesByPlayerIdAndStatus(@Param("id") Long id, @Param("status") GameStatus status);
}
