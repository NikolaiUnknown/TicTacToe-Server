package com.tictactoe.server.services.impl;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tictactoe.server.exceptions.RefreshTokenExpiredException;
import com.tictactoe.server.exceptions.RefreshTokenNotFoundException;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.models.RefreshToken;
import com.tictactoe.server.repositories.PlayerRepository;
import com.tictactoe.server.repositories.RefreshTokenRepository;
import com.tictactoe.server.security.JwtCore;
import com.tictactoe.server.security.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private JwtCore jwtCore;
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenServiceImpl;

    @Test
    void testSuccessfulGenerateRefreshToken() {
        Player player = new Player(0L,"","",new Date(),0);
        var auth = new UsernamePasswordAuthenticationToken(new UserDetailsImpl(player), null);
        assertDoesNotThrow(() -> refreshTokenServiceImpl.generateRefreshToken(auth));
        verify(refreshTokenRepository,times(1)).save(any(RefreshToken.class));
    }

    @Test
    void testSuccefulUpdateToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setPlayerId(0L);
        refreshToken.setExpiryDate(new Date(new Date().getTime() + Integer.MAX_VALUE));
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(new Player()));
        when(jwtCore.generateToken(any())).thenReturn("new jwt");
        assertEquals("new jwt",refreshTokenServiceImpl.updateToken(""));
    }

    @Test
    void testUpdateExpireToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(new Date());
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        assertThrows(RefreshTokenExpiredException.class,() -> refreshTokenServiceImpl.updateToken(""));
        verify(refreshTokenRepository).delete(refreshToken);
    }

    @Test
    void testUpdateNonExistToken() {
        assertThrows(RefreshTokenNotFoundException.class,() -> refreshTokenServiceImpl.updateToken(""));
    }

    @Test
    void testUpdateTokenWithNonExistPlayer() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(new Date(new Date().getTime() + Integer.MAX_VALUE));
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        assertThrows(UsernameNotFoundException.class,() -> refreshTokenServiceImpl.updateToken(""));
    }
}
