package com.tictactoe.server.controllers;

import com.tictactoe.server.dto.PlayerResponseDto;
import com.tictactoe.server.mappers.PlayerMapper;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/players")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    @GetMapping("/me")
    public ResponseEntity<Long> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(userDetails.getPlayer().getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDto> getPlayer(@PathVariable("id") Long id){
        var dto = playerMapper.toDto(playerService.loadPlayerById(id));
        return ResponseEntity.ok(dto);
    }


}
