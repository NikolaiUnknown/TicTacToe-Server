package com.tictactoe.server.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ConfigurationProperties(prefix = "security")
@Slf4j
public class JwtCore {
    
    @Setter
    private String secret;

    @Setter
    private int lifetime;

    private SecretKey key;

    @PostConstruct
    void init(){
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + lifetime))
                .subject(String.valueOf(userDetails.getPlayer().getId()))
                .signWith(key)        
                .compact();
    }

}
