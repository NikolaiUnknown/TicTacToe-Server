package com.tictactoe.server.repositories;

import java.util.List;
import java.util.Optional;

import com.tictactoe.server.dto.player.PlayerLastGameResultResponseDto;
import com.tictactoe.server.models.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Pair;
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

    @Query("""
            SELECT
            NEW com.tictactoe.server.dto.player.PlayerLastGameResultResponseDto(p.id, p.nickname, p.rating, g.winner.id, g.dateOfEnd)
            FROM Game g JOIN Player p
            ON p.id =
            CASE
            WHEN (g.firstPlayer.id =:id) THEN g.secondPlayer.id
            WHEN (g.secondPlayer.id =:id) THEN g.firstPlayer.id
            END
            WHERE (g.firstPlayer.id =:id
            OR g.secondPlayer.id =:id)
            AND g.status='COMPLETED'
            ORDER BY g.dateOfEnd
            DESC
            LIMIT :countOfPlayers
            """)
    List<PlayerLastGameResultResponseDto> findPlayersWhereFirstOrSecondIdIsNotId(Long id, Integer countOfPlayers);

    @Query("""
            SELECT p
            FROM Player p
            WHERE p.rating >= (:rating-:difference)
            AND p.rating <= (:rating+:difference)
            AND (p.id !=:id)
            ORDER BY p.rating
            LIMIT :countOfPlayers
            """)
    List<Player> findPlayersWhereRatingIsOnDifferent(Long id,Integer rating, Integer countOfPlayers, Integer difference);
}
