package com.tictactoe.server.services.impl;

import java.util.Date;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tictactoe.server.exceptions.NicknameIsUsedException;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.repositories.PlayerRepository;
import com.tictactoe.server.services.PlayerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;
    @Override
    public Player loadPlayerById(Long id) {
        return playerRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("Player not found!"));
    }

    @Override
    public void registerNewPlayer(Player player) {
        if (playerRepository.findPlayerByNickname(player.getNickname()).isPresent()) {
            throw new NicknameIsUsedException("Nickname %s is already use!".formatted(player.getNickname()));
        }
        player.setDateOfRegistration(new Date());
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        player.setRating(0);
        playerRepository.save(player);
    }
    
}
