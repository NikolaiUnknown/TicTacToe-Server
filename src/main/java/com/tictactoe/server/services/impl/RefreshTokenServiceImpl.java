package com.tictactoe.server.services.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tictactoe.server.exceptions.RefreshTokenExpiredException;
import com.tictactoe.server.exceptions.RefreshTokenNotFoundException;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.models.RefreshToken;
import com.tictactoe.server.repositories.PlayerRepository;
import com.tictactoe.server.repositories.RefreshTokenRepository;
import com.tictactoe.server.security.JwtCore;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.RefreshTokenService;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "security.refresh")
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService{
    
    @Setter
    private int lifetime; 
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final PlayerRepository playerRepository;
    private final JwtCore jwtCore;
    @Override
    public String generateRefreshToken(Authentication authentication) {
        var userDetails = (UserDetailsImpl)authentication.getPrincipal();
        RefreshToken token = new RefreshToken(userDetails.getPlayer().getId(),UUID.randomUUID().toString(),new Date(new Date().getTime() + lifetime));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    @Override
    public String updateToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found!"));
        if (token.getExpiryDate().compareTo(new Date()) != 1) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException("Refresh token has expired, please re-login");
        }
        Player player = playerRepository.findById(token.getPlayerId())
                    .orElseThrow(() -> new UsernameNotFoundException("Player not found!"));
        UserDetails userDetails = new UserDetailsImpl(player);
        var auth = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        return jwtCore.generateToken(auth);
    }
    
}
