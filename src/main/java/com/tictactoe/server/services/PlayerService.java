package com.tictactoe.server.services;

import org.springframework.stereotype.Service;

import com.tictactoe.server.models.Player;

@Service
public interface PlayerService {
    
    Player loadPlayerById(Long id);

    void registerNewPlayer(Player player);
}
