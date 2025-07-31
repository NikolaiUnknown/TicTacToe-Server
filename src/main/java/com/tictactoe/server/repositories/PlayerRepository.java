package com.tictactoe.server.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tictactoe.server.models.Player;
@Repository
public interface PlayerRepository extends CrudRepository<Player,Long>{
    Optional<Player> findPlayerByNickname(String nickname);
}
