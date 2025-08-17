package com.tictactoe.server.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.server.config.SecurityConfig;
import com.tictactoe.server.dto.CreateGameRequestDto;
import com.tictactoe.server.dto.GameResponseDto;
import com.tictactoe.server.enums.GameStatus;
import com.tictactoe.server.exceptions.GameNotFoundException;
import com.tictactoe.server.exceptions.InvalidGameStatusException;
import com.tictactoe.server.exceptions.PlayerNotFoundException;
import com.tictactoe.server.exceptions.SelfRequestException;
import com.tictactoe.server.mappers.GameMapper;
import com.tictactoe.server.models.Game;
import com.tictactoe.server.security.JwtCore;
import com.tictactoe.server.services.GameService;
import com.tictactoe.server.services.PlayerService;
import com.tictactoe.server.utils.TestUserDetailsService;

@WebMvcTest(GameController.class)
@Import({SecurityConfig.class, JwtCore.class, TestUserDetailsService.class})
public class GameControllerTest {

    @MockitoBean
    private GameService gameService;
    
    @MockitoBean
    private GameMapper gameMapper;

    @MockitoBean
    private PlayerService playerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    @WithUserDetails
    void testConfirmNonExistGameProposition() throws Exception {
        doThrow(new GameNotFoundException()).when(gameService).acceptProposition(0L,0L);
        mockMvc.perform(patch("/api/v1/games/confirm")
                .param("game","0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg").value("Game not found"));
    }

    @Test
    @WithUserDetails
    void testConfirmForbiddenGameProposition() throws Exception {
        doThrow(AccessDeniedException.class).when(gameService).acceptProposition(0L,0L);
        mockMvc.perform(patch("/api/v1/games/confirm")
                .param("game","0"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails
    void testConfirmAlreadyAcceptedGameProposition() throws Exception {
        doThrow(new InvalidGameStatusException(GameStatus.IN_PROCESS)).when(gameService).acceptProposition(0L,0L);
        mockMvc.perform(patch("/api/v1/games/confirm")
                .param("game","0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("This game is already started"));
    }

    @Test
    @WithUserDetails
    void testConfirmCanceledGameProposition() throws Exception {
        doThrow(new InvalidGameStatusException(GameStatus.CANCELED)).when(gameService).acceptProposition(0L,0L);
        mockMvc.perform(patch("/api/v1/games/confirm")
                .param("game","0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("This game is canceled"));
    }

    @Test
    @WithUserDetails
    void testConfirmCompletedGameProposition() throws Exception {
        doThrow(new InvalidGameStatusException(GameStatus.COMPLETED)).when(gameService).acceptProposition(0L,0L);
        mockMvc.perform(patch("/api/v1/games/confirm")
                .param("game","0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("This game is completed"));
    }

    @Test
    @WithUserDetails
    void testSuccefulConfirmGameProposition() throws Exception {
        mockMvc.perform(patch("/api/v1/games/confirm")
                .param("game","0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails
    void testGetAllGames() throws Exception {
        Game game = new Game();
        game.setId(0L);
        var gameDto = new GameResponseDto();
        gameDto.setId(0L);
        var games = List.of(game);
        when(gameService.getAllGames(0L)).thenReturn(games);
        when(gameMapper.gamesToDtos(games)).thenReturn(List.of(gameDto));
        mockMvc.perform(get("/api/v1/games/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(0L));
    }

    @Test
    @WithAnonymousUser
    void testGetAllGamesWithUnauthorizePrinciple() throws Exception {
        mockMvc.perform(get("/api/v1/games/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails
    void testGetPropositions() throws Exception {
        Game game = new Game();
        game.setId(0L);
        var gameDto = new GameResponseDto();
        gameDto.setId(0L);
        var games = List.of(game);
        when(gameService.getPropositions(0L)).thenReturn(games);
        when(gameMapper.gamesToDtos(games)).thenReturn(List.of(gameDto));
        mockMvc.perform(get("/api/v1/games/propositions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(0L));
    }

    @Test
    @WithUserDetails
    void testSuccefulProposeGame() throws Exception {
        when(gameService.createGame(0L,1L)).thenReturn(0L);
        mockMvc.perform(post("/api/v1/games/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateGameRequestDto(0L))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(0L));
    }

    @Test
    @WithUserDetails
    void testProposeGameToYourself() throws Exception {
        when(gameService.createGame(0L,0L))
                .thenThrow(new SelfRequestException());
        mockMvc.perform(post("/api/v1/games/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateGameRequestDto(0L))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("This is you"));
    }

    @Test
    @WithUserDetails
    void testProposeGameWithNonExistEnemy() throws Exception {
        CreateGameRequestDto dto = new CreateGameRequestDto(1L);
        when(gameService.createGame(0L,1L))
                .thenThrow(new PlayerNotFoundException());
        mockMvc.perform(post("/api/v1/games/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg").value("Player not found"));        
    }
}
