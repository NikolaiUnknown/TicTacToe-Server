package com.tictactoe.server.services.impl;

import com.tictactoe.server.exceptions.GameSessionNotFoundException;
import com.tictactoe.server.services.MessageCacheService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageCacheServiceImpl implements MessageCacheService {
                    //SessionId/GameId
    private final Map<String,Long> cache;

    public MessageCacheServiceImpl() {
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public void addSubscribe(String sessionId, Long gameId) {
        cache.put(sessionId,gameId);
    }

    @Override
    public Long findGameBySessionId(Long playerId,String sessionId) {
        if (cache.containsKey(sessionId)) return cache.get(sessionId);
        else throw new GameSessionNotFoundException(playerId);
    }

    @Override
    public void removeGameFromCache(Long gameId) {
        for (String key: cache.keySet()){
            if (cache.get(key).equals(gameId)){
                cache.remove(key);
            }
        }
    }
}
