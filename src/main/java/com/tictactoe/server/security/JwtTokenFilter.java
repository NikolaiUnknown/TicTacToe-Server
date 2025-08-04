package com.tictactoe.server.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tictactoe.server.models.Player;
import com.tictactoe.server.services.PlayerService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";   
    public static final String HEADER_NAME = "Authorization";

    private final JwtCore jwtCore;
    private final PlayerService playerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HEADER_NAME);
        String token = null;
        if (header != null && !header.isEmpty() && header.startsWith(BEARER_PREFIX)) {
            token = header.substring(BEARER_PREFIX.length());
        }
        if (token != null) {
            
            try {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    Long playerId = jwtCore.getIdByToken(token);
                    Player player = playerService.loadPlayerById(playerId);
                    UserDetailsImpl userDetails = new UserDetailsImpl(player);
                    var auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth); 
                }
            } catch (JwtException e) {
                //ignored
            }
        }
        filterChain.doFilter(request, response);
    }


    
}
