package com.tictactoe.server.core;

import java.util.EnumMap;
import java.util.Map;

import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.exceptions.FieldAlreadyUsedException;
import com.tictactoe.server.exceptions.PrematureMoveException;

import lombok.Getter;

public class GameSession {

    private Map<GameCoord,GameFieldValue> gameBoard;
    @Getter
    private Map<Long,GameFieldValue> players;

    private int moveCounter = 0;

    public GameSession(Long playerX, Long playerO) {
        initgameBoard();
        players = Map.of(playerX,GameFieldValue.X,playerO,GameFieldValue.O);
    }


    public GameSessionStatus move(Long playerId, GameCoord coord){
        if (!gameBoard.get(coord).equals(GameFieldValue.NONE)) {
            throw new FieldAlreadyUsedException("Field %s is already used".formatted(coord.toString()));
        }
        GameFieldValue currentValue = null;
        if (moveCounter % 2 == 0) {
            currentValue = GameFieldValue.X;
        } else {
            currentValue = GameFieldValue.O;
        }
        if (!players.get(playerId).equals(currentValue)) {
            throw new PrematureMoveException("%s is moving now".formatted(currentValue));
        }
        gameBoard.put(coord,players.get(playerId));
        moveCounter++;
        if (checkWin(GameFieldValue.X)) {
            return GameSessionStatus.X_WIN;
        } else if (checkWin(GameFieldValue.O)) {
            return GameSessionStatus.O_WIN;
        } else if (checkTie()){
            return GameSessionStatus.TIE;
        }
        return GameSessionStatus.CONTINUE;
    }

    private void initgameBoard(){
        gameBoard = new EnumMap<>(GameCoord.class);
        for (GameCoord coord: GameCoord.values()){
            gameBoard.put(coord,GameFieldValue.NONE);
        }
    }

    private boolean checkWin(GameFieldValue value){
        return (gameBoard.get(GameCoord.COORD_0_0).equals(value) &&
                gameBoard.get(GameCoord.COORD_0_1).equals(value) &&
                gameBoard.get(GameCoord.COORD_0_2).equals(value)) ||
                (gameBoard.get(GameCoord.COORD_1_0).equals(value) &&
                gameBoard.get(GameCoord.COORD_1_1).equals(value) &&
                gameBoard.get(GameCoord.COORD_1_2).equals(value)) ||
                (gameBoard.get(GameCoord.COORD_2_0).equals(value) &&
                gameBoard.get(GameCoord.COORD_2_1).equals(value) &&
                gameBoard.get(GameCoord.COORD_2_2).equals(value)) ||
                (gameBoard.get(GameCoord.COORD_0_0).equals(value) &&
                gameBoard.get(GameCoord.COORD_1_0).equals(value) &&
                gameBoard.get(GameCoord.COORD_2_0).equals(value)) ||
                (gameBoard.get(GameCoord.COORD_0_1).equals(value) &&
                gameBoard.get(GameCoord.COORD_1_1).equals(value) &&
                gameBoard.get(GameCoord.COORD_2_1).equals(value)) ||
                (gameBoard.get(GameCoord.COORD_0_2).equals(value) &&
                gameBoard.get(GameCoord.COORD_1_2).equals(value) &&
                gameBoard.get(GameCoord.COORD_2_2).equals(value)) ||
                (gameBoard.get(GameCoord.COORD_0_0).equals(value) &&
                gameBoard.get(GameCoord.COORD_1_1).equals(value) &&
                gameBoard.get(GameCoord.COORD_2_2).equals(value)) ||
                (gameBoard.get(GameCoord.COORD_0_2).equals(value) &&
                gameBoard.get(GameCoord.COORD_1_1).equals(value) &&
                gameBoard.get(GameCoord.COORD_2_0).equals(value));
    }
    private boolean checkTie(){
        return moveCounter == 9;
    }
}