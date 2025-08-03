package com.tictactoe.server.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {

    String generateRefreshToken(Authentication authentication);
    
    String updateToken(String refreshToken);
}
