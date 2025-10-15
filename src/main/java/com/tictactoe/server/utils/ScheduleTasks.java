package com.tictactoe.server.utils;

import com.tictactoe.server.core.DisconnectedPlayersManager;
import com.tictactoe.server.core.UnstartedGamesManager;
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

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class ScheduleTasks {

    @Value("${game.acceptable_disconnect_time}")
    private Long acceptableDisconnectTime;
    private final DisconnectedPlayersManager disconnectedPlayersManager;
    private final UnstartedGamesManager unstartedGamesManager;
    private final GameService gameService;

    @Scheduled(fixedRate = 1000)
    public void scheduleLoseTimer(){
        for (Pair<Long,Long> playerIdGameIdPair: disconnectedPlayersManager.getExpiredPlayers(acceptableDisconnectTime)){
            Long playerId = playerIdGameIdPair.getFirst();
            Long gameId = playerIdGameIdPair.getSecond();
            GameFieldValue value =  gameService.getPlayerValue(gameId,playerId);
            switch (value){
                    case X -> gameService.regResult(gameId, GameSessionStatus.O_WIN);
                    case O -> gameService.regResult(gameId, GameSessionStatus.X_WIN);
                }
            disconnectedPlayersManager.removeAllByGameId(gameId);
            return;
        }
    }

    @Scheduled(fixedRate = 1000)
    public void scheduleUnstartedGames(){
        var unstartedGames = unstartedGamesManager.getExpiredGames(acceptableDisconnectTime * 3);
        for (Long gameId: unstartedGames){
            gameService.cancelGame(gameId);
            unstartedGamesManager.removeFromUnstarted(gameId);
        }
    }


}
