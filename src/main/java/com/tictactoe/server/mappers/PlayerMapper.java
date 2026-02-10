package com.tictactoe.server.mappers;

import com.tictactoe.server.dto.player.PlayerResponseDto;
import org.mapstruct.*;

import com.tictactoe.server.dto.RegisterRequestDto;
import com.tictactoe.server.models.Player;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {
    @Mapping(target = "dateOfRegistration", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "proposedGames", ignore = true)
    @Mapping(target = "wonGames", ignore = true)
    @Mapping(target = "receivedGames", ignore = true)
    Player registerDtoToPlayer(RegisterRequestDto registerRequestDto);

    Player toPlayer(PlayerResponseDto playerResponseDto);

    PlayerResponseDto toDto(Player player);

    List<PlayerResponseDto> playersToDtos(List<Player> players);

}
