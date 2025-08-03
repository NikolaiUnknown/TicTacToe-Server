package com.tictactoe.server.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tictactoe.server.models.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
}
