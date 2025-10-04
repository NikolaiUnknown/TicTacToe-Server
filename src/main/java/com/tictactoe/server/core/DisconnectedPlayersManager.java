package com.tictactoe.server.core;

import com.tictactoe.server.exceptions.PlayerNotFoundException;
import com.tictactoe.server.repositories.DisconnectedPlayersRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class DisconnectedPlayersManager implements DisconnectedPlayersRepository {
    //              gameId/playerIds
    private final Map<Long, List<Long>> cache;
    //              playerId,gameId/DisconnectTime
    private final Map<Pair<Long, Long>, Long> playerDisconnectTime;

    public DisconnectedPlayersManager() {
        this.playerDisconnectTime = new ConcurrentHashMap<>();
        this.cache = new ConcurrentHashMap<>();
    }

    public Map<Pair<Long, Long>, Long> getPlayersWithDisconnectTime(){
        return this.playerDisconnectTime;
    }

    @Override
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


    @Override
    public boolean isDisconnected(Long gameId, Long playerId) {
        if (!cache.containsKey(gameId)){
            return false;
        }
        return cache.get(gameId).contains(playerId);
    }

    @Override
    public void remove(Long gameId, Long playerId) {
        if (cache.containsKey(gameId)){
            cache.get(gameId).remove(playerId);
            playerDisconnectTime.remove(Pair.of(playerId,gameId));
            return;
        }
        throw new PlayerNotFoundException();
    }

    //FIXME
    @Override
    public void removeAllByGameId(Long gameId) {
        System.out.println("cache: " + cache.get(gameId));
        var players = cache.get(gameId);
        System.out.println(players);
        for (Long playerId: players){
            playerDisconnectTime.remove(Pair.of(playerId,gameId));
            cache.get(gameId).remove(playerId);
        }
        System.out.println("new cache: " + playerDisconnectTime);
    }
}
