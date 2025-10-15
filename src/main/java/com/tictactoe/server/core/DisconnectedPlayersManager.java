package com.tictactoe.server.core;

import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.exceptions.PlayerNotFoundException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class DisconnectedPlayersManager{
    //              gameId/playerIds
    private final Map<Long, List<Long>> cache;
    //              playerId,gameId/DisconnectTime
    private final Map<Pair<Long, Long>, Long> playerDisconnectTime;

    public DisconnectedPlayersManager() {
        this.playerDisconnectTime = new ConcurrentHashMap<>();
        this.cache = new ConcurrentHashMap<>();
    }

    public Set<Pair<Long, Long>> getExpiredPlayers(Long acceptableDisconnectTime) {
        Date nowTime = new Date();
        Set<Pair<Long, Long>> playersWithExpiredTimers = new LinkedHashSet<>();

        for (Pair<Long, Long> playerIdGameIdPair : playerDisconnectTime.keySet()) {
            Long playerId = playerIdGameIdPair.getFirst();
            Long gameId = playerIdGameIdPair.getSecond();
            if (nowTime.getTime() - playerDisconnectTime.get(Pair.of(playerId, gameId)) > acceptableDisconnectTime) {
                playersWithExpiredTimers.add(playerIdGameIdPair);
            }
        }
        return playersWithExpiredTimers;
    }

    public void markDisconnected(Long gameId,Long playerId) {
        if (cache.containsKey(gameId)){
            var disconnectedPlayers = cache.get(gameId);
            disconnectedPlayers.add(playerId);
            cache.put(gameId,disconnectedPlayers);
        } else {
            cache.put(gameId, new CopyOnWriteArrayList<>(Set.of(playerId)));
        }
        playerDisconnectTime.put(Pair.of(playerId,gameId),new Date().getTime());
    }


    public boolean isDisconnected(Long gameId, Long playerId) {
        if (!cache.containsKey(gameId)){
            return false;
        }
        return cache.get(gameId).contains(playerId);
    }

    public void remove(Long gameId, Long playerId) {
        if (cache.containsKey(gameId)){
            cache.get(gameId).remove(playerId);
            playerDisconnectTime.remove(Pair.of(playerId,gameId));
            return;
        }
        throw new PlayerNotFoundException();
    }

    public void removeAllByGameId(Long gameId) {
        var players = cache.get(gameId);
        for (Long playerId: players){
            playerDisconnectTime.remove(Pair.of(playerId,gameId));
            cache.get(gameId).remove(playerId);
        }
    }
}
