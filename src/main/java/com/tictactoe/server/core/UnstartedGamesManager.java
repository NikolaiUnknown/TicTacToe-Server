package com.tictactoe.server.core;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UnstartedGamesManager {
    //              gameId/startTime
    private final Map<Long, Long> unstartedGamesCache;

    public UnstartedGamesManager() {
        this.unstartedGamesCache = new ConcurrentHashMap<>();
    }

    public void markUnstarted(Long gameId, Long startTime){
        unstartedGamesCache.put(gameId,startTime);
    }

    public Set<Long> getExpiredGames(Long acceptableDisconnectTime) {
        Date nowTime = new Date();
        Set<Long> unstartedGames = new LinkedHashSet<>();
        for (Long gameId : unstartedGamesCache.keySet()) {
            if (nowTime.getTime() - unstartedGamesCache.get(gameId) > acceptableDisconnectTime) {
                unstartedGames.add(gameId);
            }
        }
        return unstartedGames;
    }

    public boolean isUnstarted(Long gameId){
        return unstartedGamesCache.containsKey(gameId);
    }

    public void removeFromUnstarted(Long gameId){
        unstartedGamesCache.remove(gameId);
    }

}
