package com.tictactoe.server.services;

import com.tictactoe.server.dto.PlayerStatsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.tictactoe.server.models.Player;

import java.util.List;

@Service
public interface PlayerService {
    
    Player loadPlayerById(Long id);

    void registerNewPlayer(Player player);

    Page<Player> loadLeaders(int page);

    int getPlaceInLeaderboard(Long id);

    PlayerStatsResponseDto getPlayerStats(Long id);

    List<Player> getPlayerEnemies(Long id);
}
