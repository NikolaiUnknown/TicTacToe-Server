package com.tictactoe.server.repositories;

import java.util.List;
import java.util.Optional;

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
    List<Player> findPlayersOrderByRatingDesc(Pageable pageable);
}
