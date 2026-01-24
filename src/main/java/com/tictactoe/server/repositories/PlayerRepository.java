package com.tictactoe.server.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tictactoe.server.models.Player;
@Repository
public interface PlayerRepository extends CrudRepository<Player,Long>{
    Optional<Player> findPlayerByNickname(String nickname);

    @Query(value = """
            SELECT p
            FROM Player p
            ORDER BY p.rating DESC
            """)
    Page<Player> findPlayersOrderByRatingDesc(Pageable pageable);

    @Query("""
            SELECT p.id
            FROM Player p
            ORDER BY p.rating DESC
            """)
    List<Long> findPlayerIdsOrderByRatingDesc();

    @Query("""
            SELECT DISTINCT p
            FROM Game g JOIN Player p
            ON p.id =
            CASE
            WHEN (g.firstPlayer.id =:id) THEN g.secondPlayer.id
            WHEN (g.secondPlayer.id =:id) THEN g.firstPlayer.id
            END
            WHERE g.firstPlayer.id =:id
            OR g.secondPlayer.id =:id
            """)
    List<Player> findPlayerWhereFirstOrSecondIdIsNotId(Long id);
}
