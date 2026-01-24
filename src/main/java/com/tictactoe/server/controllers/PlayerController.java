package com.tictactoe.server.controllers;

import com.tictactoe.server.dto.PlayerResponseDto;
import com.tictactoe.server.dto.PlayerStatsResponseDto;
import com.tictactoe.server.mappers.PlayerMapper;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/leaders")
    public ResponseEntity<Page<PlayerResponseDto>> getLeaders(@RequestParam("page") Integer page){

        var dto = playerService.loadLeaders(page).map(playerMapper::toDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/leaders/{id}")
    public ResponseEntity<Integer> getPlaceInLeaderboard(@PathVariable("id") Long id){
        return ResponseEntity.ok(playerService.getPlaceInLeaderboard(id));
    }

    @GetMapping("/enemies/{id}")
    public ResponseEntity<List<PlayerResponseDto>> getPlayerEnemies(@PathVariable("id") Long id){
        var dto = playerMapper.playersToDtos(playerService.getPlayerEnemies(id));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<PlayerStatsResponseDto> getPlayerStats(@PathVariable("id") Long id){
        return ResponseEntity.ok(playerService.getPlayerStats(id));
    }

}
