package com.tictactoe.server.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.tictactoe.server.dto.GameResponseDto;
import com.tictactoe.server.models.Game;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GameMapper {
    GameResponseDto gameToDto(Game game);
}