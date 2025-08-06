package com.tictactoe.server.core;

import com.tictactoe.server.enums.GameCoord;
import com.tictactoe.server.enums.GameFieldValue;

public record GameField(GameCoord coord, GameFieldValue value) {
} 
