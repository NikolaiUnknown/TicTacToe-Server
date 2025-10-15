package com.tictactoe.server.services;

public interface MessageCacheService {
    void addSubscribe(String sessionId, Long gameId);

    Long findGameBySessionId(Long playerId,String sessionId);

    void removeGameFromCache(Long gameId);
}
