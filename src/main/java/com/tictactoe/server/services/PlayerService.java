package com.tictactoe.server.services;

import com.tictactoe.server.dto.player.PlayerLastGameResultResponseDto;
import com.tictactoe.server.dto.player.PlayerStatsResponseDto;
import com.tictactoe.server.models.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.tictactoe.server.models.Player;

import java.util.List;
import java.util.Set;

@Service
public interface PlayerService {
    
    Player loadPlayerById(Long id);

    List<Player> loadPlayersByIds(List<Long> ids);

    void registerNewPlayer(Player player);

    Page<Player> loadLeaders(int page);

    int getPlaceInLeaderboard(Long id);

    PlayerStatsResponseDto getPlayerStats(Long id);

    List<Player> getPlayerEnemies(Long id);

    List<PlayerLastGameResultResponseDto> getPlayerLastEnemies(Long id, Integer count);

    List<Player> getPlayersWithNearRating(Long id, Integer rating, Integer count, Integer difference);
}
