package com.tictactoe.server.services.impl;

import org.springframework.stereotype.Repository;

import com.tictactoe.server.core.GameCore;
import com.tictactoe.server.core.GameSession;
import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.exceptions.GameSessionNotFoundException;
import com.tictactoe.server.exceptions.NotSessionParticipantException;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.repositories.GameRepository;
import com.tictactoe.server.services.GameService;

import lombok.RequiredArgsConstructor;
@Repository
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
    public void move(Long playerId, Long gameId, GameCoord coord) {
        GameSession session = gameCore.findSessionById(gameId)
                .orElseThrow(() -> new GameSessionNotFoundException("Game session not found!"));
        if (!session.getPlayers().containsKey(playerId)) {
            throw new NotSessionParticipantException("Player %s isn't in session".formatted(playerId));
        }
        GameSessionStatus status = session.move(playerId, coord);
        switch(status){
            case GameSessionStatus.TIE -> {
                //TODO
                //Game registration logic
                System.out.println("TIE");

            }
            case GameSessionStatus.X_WIN -> {
                //TODO
                //Game registration logic
                System.out.println("X WINS");
            }
            case GameSessionStatus.O_WIN -> {
                //TODO
                //Game registration logic
                System.out.println("O WINS");
            }
            default -> {
                return;
            }
        }
        gameCore.deleteSessionById(gameId);
    }

    private void registerResult(GameSessionStatus status, Long gameId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerResult'");
    }
    
}
