package com.tictactoe.server.services.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tictactoe.server.core.GameCore;
import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.exceptions.GameNotFoundException;
import com.tictactoe.server.exceptions.GameSessionNotFoundException;
import com.tictactoe.server.exceptions.NotSessionParticipantException;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.repositories.GameRepository;
import com.tictactoe.server.services.GameService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

    private final GameRepository gameRepository;
    private final GameCore gameCore;
    @Override
    public void createGame(Game game) {
        gameRepository.save(game);
    }

    @Override
    public GameSession createGameSession(Game game) {
        return gameCore.createNewGameSession(game);
    }

    @Override
    @Transactional
    public void move(Long playerId, Long gameId, GameCoord coord) {
        GameSession session = gameCore.findSessionById(gameId)
                .orElseThrow(() -> new GameSessionNotFoundException("Game session not found!"));
        if (!session.getPlayers().containsKey(playerId)) {
            throw new NotSessionParticipantException("Player %s isn't in session".formatted(playerId));
        }
        GameSessionStatus status = session.move(playerId, coord);
        if (status.equals(GameSessionStatus.CONTINUE)) {
            return;
        }
        else {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new GameNotFoundException("Game not found!"));
            game.setDateOfEnd(new Date());
            game.setStatus(GameStatus.COMPLETED);
            switch(status){
                case O_WIN -> game.setWinner(game.getSecondPlayer());
                case X_WIN -> game.setWinner(game.getFirstPlayer());
            }
            gameRepository.save(game);
            deleteGameSession(gameId);
        }
    }

    private void deleteGameSession(Long gameId){
        gameCore.deleteSessionById(gameId);
    }
    
}
