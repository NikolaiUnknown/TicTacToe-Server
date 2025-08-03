package com.tictactoe.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tictactoe.server.dto.JwtResponseDto;
import com.tictactoe.server.dto.LoginRequestDto;
import com.tictactoe.server.dto.RefreshTokenRequestDto;
import com.tictactoe.server.dto.RegisterRequestDto;
import com.tictactoe.server.exceptions.InvalidRequestBodyException;
import com.tictactoe.server.mappers.PlayerMapper;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.security.JwtCore;
import com.tictactoe.server.services.PlayerService;
import com.tictactoe.server.services.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    
    private final AuthenticationManager authManager;
    private final JwtCore jwtCore;
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDto> signIn(@RequestBody LoginRequestDto loginRequestDto){
        Authentication auth =  authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getNickname(),loginRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        var jwtResponseDto = new JwtResponseDto(jwtCore.generateToken(auth),refreshTokenService.generateRefreshToken(auth));
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid RegisterRequestDto registerRequestDto,
                                           BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new InvalidRequestBodyException(bindingResult);
        Player player = playerMapper.registerDtoToPlayer(registerRequestDto);
        playerService.registerNewPlayer(player);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@RequestBody RefreshTokenRequestDto refreshToken){
        String jwt = refreshTokenService.updateToken(refreshToken.token());
        return ResponseEntity.ok(new JwtResponseDto(jwt,refreshToken.token()));
    }
}
