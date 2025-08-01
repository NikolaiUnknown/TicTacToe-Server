package com.tictactoe.server.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.tictactoe.server.dto.RegisterRequestDto;
import com.tictactoe.server.models.Player;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {
    
    Player registerDtoToPlayer(RegisterRequestDto registerRequestDto);
}
