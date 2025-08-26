package com.tictactoe.server.services.impl;

import com.tictactoe.server.core.GameCore;
import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.dto.MoveMessageDto;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.exceptions.*;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.repositories.GameRepository;
import com.tictactoe.server.repositories.PlayerRepository;
import com.tictactoe.server.services.GameService;
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
        return gameCore.createNewGameSession(game);
    }

    @Override
    @Transactional
    public void move(Long playerId, Long gameId, GameCoord coord) {
        GameSession session = gameCore.findSessionById(gameId)
                .orElseThrow(GameSessionNotFoundException::new);
        if (!session.getPlayers().containsKey(playerId)) {
            throw new NotSessionParticipantException(playerId);
        }
        GameSessionStatus status = session.move(playerId, coord);
        if (status.equals(GameSessionStatus.CONTINUE)) {
            webSocketMessagingService.sendMoveMessage(new MoveMessageDto(playerId,coord),gameId);
        }
        else {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(GameNotFoundException::new);
            game.setDateOfEnd(new Date());
            game.setStatus(GameStatus.COMPLETED);
            switch(status){
                case O_WIN -> {
                    Player winner = game.getSecondPlayer();
                    Player loser = game.getFirstPlayer();
                    game.setWinner(winner);
                    winner.setRating(winner.getRating() + ratingIncrease);
                    loser.setRating(loser.getRating() - ratingIncrease);
                    if (loser.getRating() < 0 ) {
                        loser.setRating(0);
                    }
                    playerRepository.saveAll(List.of(winner,loser));
                }
                case X_WIN -> {
                    Player winner = game.getFirstPlayer();
                    Player loser = game.getSecondPlayer();
                    game.setWinner(winner);
                    winner.setRating(winner.getRating() + ratingIncrease);
                    loser.setRating(loser.getRating() - ratingIncrease);
                    if (loser.getRating() < 0 ) {
                        loser.setRating(0);
                    }
                    playerRepository.saveAll(List.of(winner,loser));
                }
            }
            gameRepository.save(game);
            deleteGameSession(gameId);
        }
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
    public List<Game> getPropositions(Long userId) {
        return gameRepository.findAllGamesBySecondPlayerIdAndStatus(userId,GameStatus.PROPOSED);
    }

    @Override
    public List<Game> getAllGames(Long id) {
        return gameRepository.findGamesByPlayerId(id);
    }
    
}
