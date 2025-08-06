package com.tictactoe.server.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameFieldValue;
import com.tictactoe.server.enums.GameSessionStatus;
import com.tictactoe.server.exceptions.FieldAlreadyUsedException;
import com.tictactoe.server.exceptions.PrematureMoveException;

public class GameSession {

    private Map<GameCoord,GameFieldValue> gameBoard;

    private Map<Long,GameFieldValue> players;

    private int moveCounter = 0;

    public GameSession(Long playerX, Long playerO) {
        initBoard();
        players = new ConcurrentHashMap<>();
        players.put(playerX,GameFieldValue.X);
        players.put(playerO,GameFieldValue.O);             
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
        return checkGameSessionStatus(gameBoard);
    }
    //FIXME
    public String printGameBorder(){
        char[] gameValues =new char[9];
        GameFieldValue value = gameBoard.get(GameCoord.COORD_0_0);
            switch(value){
                case GameFieldValue.X -> {
                    gameValues[0] = 'X';
                    break;
                }
                case GameFieldValue.O -> {
                    gameValues[0] = 'O';
                    break;
                }
                case GameFieldValue.NONE -> {
                    gameValues[0] = ' ';
                    break;
                }
            }
        value = gameBoard.get(GameCoord.COORD_0_1);
            switch(value){
                case GameFieldValue.X -> {
                    gameValues[1] = 'X';
                    break;
                }
                case GameFieldValue.O -> {
                    gameValues[1] = 'O';
                    break;
                }
                case GameFieldValue.NONE -> {
                    gameValues[1] = ' ';
                    break;
                }
            } 
            value = gameBoard.get(GameCoord.COORD_0_2);
            switch(value){
                case GameFieldValue.X -> {
                    gameValues[2] = 'X';
                    break;
                }
                case GameFieldValue.O -> {
                    gameValues[2] = 'O';
                    break;
                }
                case GameFieldValue.NONE -> {
                    gameValues[2] = ' ';
                    break;
                }
            } 
            value = gameBoard.get(GameCoord.COORD_1_0);
            switch(value){
                case GameFieldValue.X -> {
                    gameValues[3] = 'X';
                    break;
                }
                case GameFieldValue.O -> {
                    gameValues[3] = 'O';
                    break;
                }
                case GameFieldValue.NONE -> {
                    gameValues[3] = ' ';
                    break;
                }
            } 
            value = gameBoard.get(GameCoord.COORD_1_1);
            switch(value){
                case GameFieldValue.X -> {
                    gameValues[4] = 'X';
                    break;
                }
                case GameFieldValue.O -> {
                    gameValues[4] = 'O';
                    break;
                }
                case GameFieldValue.NONE -> {
                    gameValues[4] = ' ';
                    break;
                }
            } 
            value = gameBoard.get(GameCoord.COORD_1_2);
            switch(value){
                case GameFieldValue.X -> {
                    gameValues[5] = 'X';
                    break;
                }
                case GameFieldValue.O -> {
                    gameValues[5] = 'O';
                    break;
                }
                case GameFieldValue.NONE -> {
                    gameValues[5] = ' ';
                    break;
                }
            } 
            value = gameBoard.get(GameCoord.COORD_2_0);
            switch(value){
                case GameFieldValue.X -> {
                    gameValues[6] = 'X';
                    break;
                }
                case GameFieldValue.O -> {
                    gameValues[6] = 'O';
                    break;
                }
                case GameFieldValue.NONE -> {
                    gameValues[6] = ' ';
                    break;
                }
            } 
            value = gameBoard.get(GameCoord.COORD_2_1);
            switch(value){
                case GameFieldValue.X -> {
                    gameValues[7] = 'X';
                    break;
                }
                case GameFieldValue.O -> {
                    gameValues[7] = 'O';
                    break;
                }
                case GameFieldValue.NONE -> {
                    gameValues[7] = ' ';
                    break;
                }
            } 
            value = gameBoard.get(GameCoord.COORD_2_2);
            switch(value){
                case GameFieldValue.X -> {
                    gameValues[8] = 'X';
                    break;
                }
                case GameFieldValue.O -> {
                    gameValues[8] = 'O';
                    break;
                }
                case GameFieldValue.NONE -> {
                    gameValues[8] = ' ';
                    break;
                }
            } 
 
        String boarderToString = """
                       |     |     
                    %s  |  %s  |  %s  
                  -----------------
                       |     |     
                    %s  |  %s  |  %s  
                  -----------------
                       |     |     
                    %s  |  %s  |  %s  
                 """.formatted(gameValues[0],gameValues[1],gameValues[2],gameValues[3],gameValues[4],gameValues[5],gameValues[6],gameValues[7],gameValues[8]);

                System.out.println(players);
        return (boarderToString);
    }

    private void initBoard(){
        gameBoard = new ConcurrentHashMap<>();
        gameBoard.put(GameCoord.COORD_0_0,GameFieldValue.NONE);
        gameBoard.put(GameCoord.COORD_0_1,GameFieldValue.NONE);  
        gameBoard.put(GameCoord.COORD_0_2,GameFieldValue.NONE);
        gameBoard.put(GameCoord.COORD_1_0,GameFieldValue.NONE);
        gameBoard.put(GameCoord.COORD_1_1,GameFieldValue.NONE);
        gameBoard.put(GameCoord.COORD_1_2,GameFieldValue.NONE);
        gameBoard.put(GameCoord.COORD_2_0,GameFieldValue.NONE);
        gameBoard.put(GameCoord.COORD_2_1,GameFieldValue.NONE);
        gameBoard.put(GameCoord.COORD_2_2,GameFieldValue.NONE);
    }

    private GameSessionStatus checkGameSessionStatus(Map<GameCoord,GameFieldValue> board){
        if ((board.get(GameCoord.COORD_0_0).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_0_1).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_0_2).equals(GameFieldValue.X)) ||
            (board.get(GameCoord.COORD_1_0).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_1_1).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_1_2).equals(GameFieldValue.X)) ||
            (board.get(GameCoord.COORD_2_0).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_2_1).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_2_2).equals(GameFieldValue.X)) ||
            (board.get(GameCoord.COORD_0_0).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_1_0).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_2_0).equals(GameFieldValue.X)) ||
            (board.get(GameCoord.COORD_0_1).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_1_1).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_2_1).equals(GameFieldValue.X)) ||
            (board.get(GameCoord.COORD_0_2).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_1_2).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_2_2).equals(GameFieldValue.X)) ||
            (board.get(GameCoord.COORD_0_0).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_1_1).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_2_2).equals(GameFieldValue.X)) ||
            (board.get(GameCoord.COORD_0_2).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_1_1).equals(GameFieldValue.X) &&
            board.get(GameCoord.COORD_2_0).equals(GameFieldValue.X))
            ) {
            return GameSessionStatus.X_WIN;
        } else if ((board.get(GameCoord.COORD_0_0).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_0_1).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_0_2).equals(GameFieldValue.O)) ||
            (board.get(GameCoord.COORD_1_0).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_1_1).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_1_2).equals(GameFieldValue.O)) ||
            (board.get(GameCoord.COORD_2_0).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_2_1).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_2_2).equals(GameFieldValue.O)) ||
            (board.get(GameCoord.COORD_0_0).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_1_0).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_2_0).equals(GameFieldValue.O)) ||
            (board.get(GameCoord.COORD_0_1).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_1_1).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_2_1).equals(GameFieldValue.O)) ||
            (board.get(GameCoord.COORD_0_2).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_1_2).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_2_2).equals(GameFieldValue.O)) ||
            (board.get(GameCoord.COORD_0_0).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_1_1).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_2_2).equals(GameFieldValue.O)) ||
            (board.get(GameCoord.COORD_0_2).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_1_1).equals(GameFieldValue.O) &&
            board.get(GameCoord.COORD_2_0).equals(GameFieldValue.O))
            ) {
            return GameSessionStatus.O_WIN;
        } else if (moveCounter == 9) {
            return GameSessionStatus.TIE;
        }
        return GameSessionStatus.CONTINUE;
    }
}
