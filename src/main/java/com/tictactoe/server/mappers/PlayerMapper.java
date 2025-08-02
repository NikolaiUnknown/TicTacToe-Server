package com.tictactoe.server.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.tictactoe.server.dto.RegisterRequestDto;
import com.tictactoe.server.models.Player;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {
    @Mapping(target = "dateOfRegistration", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    Player registerDtoToPlayer(RegisterRequestDto registerRequestDto);
}
