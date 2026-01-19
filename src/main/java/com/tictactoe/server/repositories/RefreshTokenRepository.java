package com.tictactoe.server.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tictactoe.server.models.RefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByPlayerId(Long playerId);
}
