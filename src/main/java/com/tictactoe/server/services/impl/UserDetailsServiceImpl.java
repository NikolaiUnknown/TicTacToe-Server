package com.tictactoe.server.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tictactoe.server.models.Player;
import com.tictactoe.server.repositories.PlayerRepository;
import com.tictactoe.server.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PlayerRepository playerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findPlayerByNickname(username)
            .orElseThrow(() -> new UsernameNotFoundException("Player not found"));
        return new UserDetailsImpl(player);
    }
    
}
