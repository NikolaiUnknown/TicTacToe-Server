package com.tictactoe.server.controllers;

import com.tictactoe.server.dto.player.LeaderResponseDto;
import com.tictactoe.server.dto.player.PlayerLastGameResultResponseDto;
import com.tictactoe.server.dto.player.PlayerResponseDto;
import com.tictactoe.server.dto.player.PlayerStatsResponseDto;
import com.tictactoe.server.mappers.PlayerMapper;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<Page<LeaderResponseDto>> getLeaders(@RequestParam("page") Integer page){
        var dto = playerService.loadLeaders(page).map(
                (Player player) -> new LeaderResponseDto(playerMapper.toDto(player),
                        playerService.getPlayerStats(player.getId()))
        );
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

    @GetMapping("/enemies/last")
    public ResponseEntity<List<PlayerLastGameResultResponseDto>> getPlayerLastEnemies(
            @RequestParam(value = "count", defaultValue = "5") Integer count,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        var dto = playerService.getPlayerLastEnemies(userDetails.getPlayer().getId(), count);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/")
    public ResponseEntity<List<PlayerResponseDto>> getPlayersByIds(@RequestParam("ids") List<Long> playersIds){
        var dto = playerMapper.playersToDtos(playerService.loadPlayersByIds(playersIds));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/matchmaking")
    public ResponseEntity<List<PlayerResponseDto>> matchmaking(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "count", defaultValue = "15") Integer count,
            @RequestParam(value = "diff", defaultValue = "250") Integer difference){
        var dto = playerMapper.playersToDtos(
                playerService.getPlayersWithNearRating(
                        userDetails.getPlayer().getId(),userDetails.getPlayer().getRating(), count, difference
                )
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<PlayerStatsResponseDto> getPlayerStats(@PathVariable("id") Long id){
        return ResponseEntity.ok(playerService.getPlayerStats(id));
    }

}
