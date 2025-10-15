package com.tictactoe.server.services.impl;

import com.tictactoe.server.core.GameCore;
import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.core.UnstartedGamesManager;
import com.tictactoe.server.dto.GameSessionStatusMessageDto;
import com.tictactoe.server.dto.MoveMessageDto;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.exceptions.*;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.repositories.GameRepository;
import com.tictactoe.server.repositories.PlayerRepository;
import com.tictactoe.server.services.GameService;
import com.tictactoe.server.services.MessageCacheService;
import com.tictactoe.server.services.WebSocketMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

    private final GameRepository gameRepository;
    private final GameCore gameCore;
    private final PlayerRepository playerRepository;
    private final WebSocketMessagingService webSocketMessagingService;
    private final MessageCacheService messageCacheService;
    private final UnstartedGamesManager unstartedGamesManager;


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

    private GameSession createGameSession(Game game) {
        unstartedGamesManager.markUnstarted(game.getId(),game.getDateOfStart().getTime());
        return gameCore.createNewGameSession(game);
    }

    @Override
    @Transactional
    public void move(Long playerId, Long gameId, GameCoord coord) {
        GameSession session = gameCore.findSessionById(gameId)
                .orElseThrow(() -> new GameSessionNotFoundException(playerId));
        if (!session.getPlayers().containsKey(playerId)) {
            throw new NotSessionParticipantException(playerId);
        }
        GameSessionStatus status = session.move(playerId, coord);
        webSocketMessagingService.sendMoveMessage(new MoveMessageDto(playerId,coord),gameId);
        if (!status.equals(GameSessionStatus.CONTINUE)) {
            regResult(gameId,status);
        }
    }
    @Override
    public void regResult(Long gameId, GameSessionStatus status){
        messageCacheService.removeGameFromCache(gameId);
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotFoundException::new);
        game.setDateOfEnd(new Date());
        game.setStatus(GameStatus.COMPLETED);
        switch(status){
            case O_WIN -> {
                Player winner = game.getSecondPlayer();
                Player loser = game.getFirstPlayer();
                regWin(winner,loser,game);
            }
            case X_WIN -> {
                Player winner = game.getFirstPlayer();
                Player loser = game.getSecondPlayer();
                regWin(winner,loser,game);
            }
        }
        gameRepository.save(game);
        deleteGameSession(gameId);
        webSocketMessagingService.sendGameStatusMessage(new GameSessionStatusMessageDto(status),gameId);
    }

    private void regWin(Player winner, Player loser, Game game){
        winner.setRating(winner.getRating() + ratingIncrease);
        loser.setRating(loser.getRating() - ratingIncrease);
        game.setWinner(winner);
        if (loser.getRating() < 0) {
            loser.setRating(0);
        }
        playerRepository.saveAll(List.of(winner,loser));
    }

    private void deleteGameSession(Long gameId){
        gameCore.deleteSessionById(gameId);
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
            return createGameSession(game);
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
            if (gameCore.findSessionById(gameId).isPresent()){
                deleteGameSession(gameId);
            }
        } else{
            throw new InvalidGameStatusException(game.getStatus());
        }
    }

    @Override
    public List<Game> getPropositions(Long userId) {
        return gameRepository.findAllGamesBySecondPlayerIdAndStatus(userId,GameStatus.PROPOSED);
    }

    @Override
    public List<Game> getAllGames(Long id) {
        return gameRepository.findGamesByPlayerId(id);
    }

    @Override
    public GameFieldValue getPlayerValue(Long gameId, Long playerId){
        var gameSession = gameCore.findSessionById(gameId).orElseThrow(() -> new GameSessionNotFoundException(playerId));
        return gameSession.getPlayers().getOrDefault(playerId, GameFieldValue.NONE);
    }
}
