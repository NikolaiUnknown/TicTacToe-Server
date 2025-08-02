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

import com.tictactoe.server.dto.LoginRequestDto;
import com.tictactoe.server.dto.RegisterRequestDto;
import com.tictactoe.server.exceptions.InvalidRequestBodyException;
import com.tictactoe.server.mappers.PlayerMapper;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.security.JwtCore;
import com.tictactoe.server.services.PlayerService;

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

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody LoginRequestDto loginRequestDto){
        Authentication auth =  authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getNickname(),loginRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return ResponseEntity.ok(jwtCore.generateToken(auth));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid RegisterRequestDto registerRequestDto,
                                           BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new InvalidRequestBodyException(bindingResult);
        Player player = playerMapper.registerDtoToPlayer(registerRequestDto);
        playerService.registerNewPlayer(player);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
