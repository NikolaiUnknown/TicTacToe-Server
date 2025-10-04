package com.tictactoe.server.repositories;

public interface DisconnectedPlayersRepository {

    void markDisconnected(Long gameId, Long playerId);

    boolean isDisconnected(Long gameId, Long playerId);

    void remove(Long gameId, Long playerId);

    void removeAllByGameId(Long gameId);
}
