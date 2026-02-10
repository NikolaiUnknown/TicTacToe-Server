package com.tictactoe.server.services.impl;

import java.util.*;

import com.tictactoe.server.dto.player.PlayerLastGameResultResponseDto;
import com.tictactoe.server.dto.player.PlayerStatsResponseDto;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.models.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tictactoe.server.exceptions.NicknameIsUsedException;
import com.tictactoe.server.exceptions.PlayerNotFoundException;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.repositories.PlayerRepository;
import com.tictactoe.server.services.PlayerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;
    @Override
    public Player loadPlayerById(Long id) {
        return playerRepository.findById(id)
            .orElseThrow(PlayerNotFoundException::new);
    }

    @Override
    public List<Player> loadPlayersByIds(List<Long> ids) {
        var players = new ArrayList<Player>();
        playerRepository.findAllById(ids).forEach(players::add);
        return players;
    }

    @Override
    public void registerNewPlayer(Player player) {
        if (playerRepository.findPlayerByNickname(player.getNickname()).isPresent()) {
            throw new NicknameIsUsedException(player.getNickname());
        }
        player.setDateOfRegistration(new Date());
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        player.setRating(0);
        playerRepository.save(player);
    }

    @Override
    public Page<Player> loadLeaders(int page) {
        return playerRepository.findPlayersOrderByRatingDesc(PageRequest.of(page,10));
    }

    @Override
    public int getPlaceInLeaderboard(Long id) {
        var playerIds = playerRepository.findPlayerIdsOrderByRatingDesc();
        for (int i = 0; i < playerIds.size(); i++) {
            if (playerIds.get(i).equals(id)){
                return i+1;
            }
        }
        throw new PlayerNotFoundException();
    }

    @Override
    public PlayerStatsResponseDto getPlayerStats(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(PlayerNotFoundException::new);
        int loses = 0;
        int ties = 0;
        int wins = 0;
        player.getProposedGames().addAll(player.getReceivedGames());
        for (Game game: player.getProposedGames()){
            if (game.getStatus().equals(GameStatus.COMPLETED)){
                if (game.getWinner() == null){
                    ties++;
                } else {
                    if (game.getWinner().getId().equals(id)){
                        wins++;
                    } else {
                        loses++;
                    }
                }

            }
        }
        return new PlayerStatsResponseDto(loses+ties+wins,wins,loses,ties);
    }

    @Override
    public List<Player> getPlayerEnemies(Long id) {
        return playerRepository.findPlayerWhereFirstOrSecondIdIsNotId(id);
    }

    @Override
    public List<PlayerLastGameResultResponseDto> getPlayerLastEnemies(Long id, Integer count) {
        return playerRepository.findPlayersWhereFirstOrSecondIdIsNotId(id, count);
    }

    @Override
    public List<Player> getPlayersWithNearRating(Long id, Integer rating, Integer count, Integer difference) {
        var players = playerRepository.findPlayersWhereRatingIsOnDifferent(id,rating,count,difference);
        Collections.shuffle(players);
        return players;
    }

}
