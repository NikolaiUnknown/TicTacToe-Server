package com.tictactoe.server.services.impl;

import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.exceptions.GameNotFoundException;
import com.tictactoe.server.exceptions.InvalidGameStatusException;
import com.tictactoe.server.exceptions.PlayerNotFoundException;
import com.tictactoe.server.exceptions.SelfRequestException;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.repositories.GameRepository;
import com.tictactoe.server.repositories.PlayerRepository;
import com.tictactoe.server.services.GameService;
import com.tictactoe.server.services.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameSessionService gameSessionService;

    @Value("${game.rating_increase}")
    private int ratingIncrease;
    
    @Override
    @Transactional
    public Long createGame(Long firstPlayerId, Long secondPlayerId) {
        if (firstPlayerId.equals(secondPlayerId)) throw new SelfRequestException();
        Player player1 = playerRepository.findById(firstPlayerId)
                    .orElseThrow(PlayerNotFoundException::new);
        Player player2 = playerRepository.findById(secondPlayerId)
                    .orElseThrow(PlayerNotFoundException::new);
        Game game = Game.builder()
                        .firstPlayer(player1)
                        .secondPlayer(player2)
                        .dateOfStart(new Date())
                        .status(GameStatus.PROPOSED)
                        .build();
        gameRepository.save(game);
        return game.getId();
    }

    @Override
    public void registerGameResult(Long gameId, GameSessionStatus status){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotFoundException::new);
        game.setDateOfEnd(new Date());
        game.setStatus(GameStatus.COMPLETED);
        switch(status){
            case O_WIN -> {
                Player winner = game.getSecondPlayer();
                Player loser = game.getFirstPlayer();
                registerWin(winner,loser,game);
            }
            case X_WIN -> {
                Player winner = game.getFirstPlayer();
                Player loser = game.getSecondPlayer();
                registerWin(winner,loser,game);
            }
        }
        gameRepository.save(game);
        gameSessionService.registerGameSessionResult(gameId,status);
    }

    private void registerWin(Player winner, Player loser, Game game){
        winner.setRating(winner.getRating() + ratingIncrease);
        loser.setRating(loser.getRating() - ratingIncrease);
        game.setWinner(winner);
        if (loser.getRating() < 0) {
            loser.setRating(0);
        }
        playerRepository.saveAll(List.of(winner,loser));
    }


    @Override
    public GameSession acceptProposition(Long gameId, Long playerId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotFoundException::new);
        if (!game.getSecondPlayer().getId().equals(playerId)) {
            throw new AccessDeniedException("Game %s is forbidden for you".formatted(gameId));
        }
        if (game.getStatus().equals(GameStatus.PROPOSED)) {
            game.setStatus(GameStatus.IN_PROCESS);
            gameRepository.save(game);
            return gameSessionService.createGameSession(game);
        } else{
            throw new InvalidGameStatusException(game.getStatus());
        }
    }

    @Override
    public void cancelGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotFoundException::new);
        if (game.getStatus().equals(GameStatus.PROPOSED) || game.getStatus().equals(GameStatus.IN_PROCESS)) {
            game.setStatus(GameStatus.CANCELED);
            gameRepository.save(game);
            gameSessionService.deleteGameSession(gameId);
        } else{
            throw new InvalidGameStatusException(game.getStatus());
        }
    }

    @Override
    public List<Game> getGamesHistory(Long userId) {
        var gamesHistory = new LinkedHashSet<Game>();
        gamesHistory.addAll(gameRepository.findGamesByPlayerIdAndStatus(userId,GameStatus.PROPOSED));
        gamesHistory.addAll(gameRepository.findGamesByPlayerIdAndStatus(userId,GameStatus.IN_PROCESS));
        gamesHistory.addAll(gameRepository.findGamesByPlayerId(userId));
        return new ArrayList<>(gamesHistory);
    }

    @Override
    public List<Game> getPropositions(Long userId) {
        return gameRepository.findAllGamesBySecondPlayerIdAndStatus(userId,GameStatus.PROPOSED);
    }

    @Override
    public List<Game> getAllGames(Long id) {
        return gameRepository.findGamesByPlayerId(id);
    }

}
