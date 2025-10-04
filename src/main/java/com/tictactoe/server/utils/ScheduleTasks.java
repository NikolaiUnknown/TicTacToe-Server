package com.tictactoe.server.utils;

import com.tictactoe.server.core.DisconnectedPlayersManager;
import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.services.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class ScheduleTasks {

    @Value("${game.acceptable_disconnect_time}")
    private Long acceptableDisconnectTime;
    private final DisconnectedPlayersManager disconnectedPlayersManager;
    private final GameService gameService;

    @Scheduled(fixedRate = 1000)
    public void scheduleLoseTimer(){
        Date nowTime = new Date();
        var disconnectedPlayers = disconnectedPlayersManager.getPlayersWithDisconnectTime();
        for (Pair<Long,Long> playerIdGameIdPair: disconnectedPlayers.keySet()){
            Long playerId = playerIdGameIdPair.getFirst();
            Long gameId = playerIdGameIdPair.getSecond();
            if (nowTime.getTime() - disconnectedPlayers.get(Pair.of(playerId,gameId)) > acceptableDisconnectTime){
                GameFieldValue value =  gameService.getPlayerValue(gameId,playerId);
                switch (value){
                    case X -> gameService.regResult(gameId, GameSessionStatus.O_WIN);
                    case O -> gameService.regResult(gameId, GameSessionStatus.X_WIN);
                }
                disconnectedPlayersManager.removeAllByGameId(gameId);
                return;
            }
        }
    }


}
