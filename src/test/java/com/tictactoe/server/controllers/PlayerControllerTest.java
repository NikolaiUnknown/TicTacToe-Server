package com.tictactoe.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.server.config.SecurityConfig;
import com.tictactoe.server.dto.PlayerResponseDto;
import com.tictactoe.server.exceptions.PlayerNotFoundException;
import com.tictactoe.server.mappers.PlayerMapper;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.security.JwtCore;
import com.tictactoe.server.services.PlayerService;
import com.tictactoe.server.services.WebSocketMessagingService;
import com.tictactoe.server.utils.TestUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
@Import({SecurityConfig.class, JwtCore.class, TestUserDetailsService.class})
class PlayerControllerTest {

    @MockitoBean
    private PlayerService playerService;

    @MockitoBean
    private PlayerMapper playerMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WebSocketMessagingService webSocketMessagingService;


    @Test
    @WithUserDetails
    void successfulGetMe() throws Exception {
        mockMvc.perform(get("/api/v1/players/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0L));
    }

    @Test
    void getMeWithNullPrinciple() throws Exception {
        mockMvc.perform(get("/api/v1/players/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void successfulGetPlayer() throws Exception {
        var responseDto = new PlayerResponseDto(0L,"", new Date(), 0);
        when(playerService.loadPlayerById(0L)).thenReturn(new Player(0L));
        when(playerMapper.toDto(any(Player.class))).thenReturn(responseDto);
        mockMvc.perform(get("/api/v1/players/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0L));
    }


    @Test
    void getNonExistPlayer() throws Exception {
        when(playerService.loadPlayerById(0L)).thenThrow(new PlayerNotFoundException());
        mockMvc.perform(get("/api/v1/players/0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg").value("Player not found"));
    }
}