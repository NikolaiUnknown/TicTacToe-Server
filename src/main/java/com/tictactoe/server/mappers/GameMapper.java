package com.tictactoe.server.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.tictactoe.server.dto.GameResponseDto;
import com.tictactoe.server.models.Game;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GameMapper {

    @Mapping(target = "firstPlayerId", source =  "firstPlayer.id")
    @Mapping(target = "secondPlayerId", source =  "secondPlayer.id")
    @Mapping(target = "winnerId", source =  "winner.id")
    GameResponseDto gameToDto(Game game);

    List<GameResponseDto> gamesToDtos(List<Game> games);

}