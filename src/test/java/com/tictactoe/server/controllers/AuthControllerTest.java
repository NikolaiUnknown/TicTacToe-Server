package com.tictactoe.server.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.server.config.SecurityConfig;
import com.tictactoe.server.dto.LoginRequestDto;
import com.tictactoe.server.dto.RefreshTokenRequestDto;
import com.tictactoe.server.dto.RegisterRequestDto;
import com.tictactoe.server.exceptions.NicknameIsUsedException;
import com.tictactoe.server.exceptions.RefreshTokenExpiredException;
import com.tictactoe.server.exceptions.RefreshTokenNotFoundException;
import com.tictactoe.server.mappers.PlayerMapper;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.security.JwtCore;
import com.tictactoe.server.services.PlayerService;
import com.tictactoe.server.services.RefreshTokenService;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @MockitoBean
    private JwtCore jwtCore;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private PlayerService playerService;

    @MockitoBean
    private PlayerMapper playerMapper;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSuccessfulSignIn() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("nickname","pass");
        when(jwtCore.generateToken(any())).thenReturn("access token");
        when(refreshTokenService.generateRefreshToken(any())).thenReturn("refresh token");
        mockMvc.perform(post("/api/v1/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("access token"))
                    .andExpect(jsonPath("$.refreshToken").value("refresh token"));
    }

    @Test
    void testSignInwithIncorrectCredincials() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("nickname","pass");
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getNickname(),loginRequestDto.getPassword())
            )).thenThrow(BadCredentialsException.class);
        mockMvc.perform(post("/api/v1/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequestDto)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorMsg").value("Nickname or password is incorrect!"));
    }

    @Test
    void testSuccessfulSignUp() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("nickname","password");
        when(playerMapper.registerDtoToPlayer(registerRequestDto)).thenReturn(new Player());
        mockMvc.perform(post("/api/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequestDto)))
                    .andExpect(status().isCreated());
        verify(playerService).registerNewPlayer(any(Player.class));
    }
    @Test
    void testSignUpWithAlreadyUsedNickname() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("nickname","password");
        when(playerMapper.registerDtoToPlayer(registerRequestDto)).thenReturn(new Player());
        var exception = new NicknameIsUsedException("Nickname test is already use!");
        doThrow(exception).when(playerService).registerNewPlayer(any(Player.class));
        mockMvc.perform(post("/api/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMsg").value(exception.getMessage()));
    }

    @Test
    void testSignUpWithInvalidNickname() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("nic","password");
        mockMvc.perform(post("/api/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMsg").value("Nickname length must be more than 4;\n"));
    }

    @Test
    void testSignUpWithInvalidPassword() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("nickname","pass");
        mockMvc.perform(post("/api/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMsg").value("Password length must be more than 6;\n"));
    }

    @Test
    void testSuccessfulRefreshToken() throws Exception {
        RefreshTokenRequestDto requestDto = new RefreshTokenRequestDto("reshresh token");
        when(refreshTokenService.updateToken(requestDto.token())).thenReturn("new access token");
        mockMvc.perform(post("/api/v1/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("new access token"))
                    .andExpect(jsonPath("$.refreshToken").value(requestDto.token()));
    }

    @Test
    void testRefreshWithExpiredToken() throws Exception {
        RefreshTokenRequestDto requestDto = new RefreshTokenRequestDto("expired token");
        String errorMessage = "Refresh token has expired, please re-login";
        when(refreshTokenService.updateToken(requestDto.token()))
                    .thenThrow(new RefreshTokenExpiredException(errorMessage));
        mockMvc.perform(post("/api/v1/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMsg").value(errorMessage));
    }

    @Test
    void testRefreshWithNonExistToken() throws Exception {
        RefreshTokenRequestDto requestDto = new RefreshTokenRequestDto("non exist token");
        String errorMessage = "Refresh token not found!";
        when(refreshTokenService.updateToken(requestDto.token()))
                    .thenThrow(new RefreshTokenNotFoundException(errorMessage));
        mockMvc.perform(post("/api/v1/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMsg").value(errorMessage));
    }

    @Test
    void testRefreshWithNonExistPlayer() throws Exception {
        RefreshTokenRequestDto requestDto = new RefreshTokenRequestDto("refresh token");
        String errorMessage = "Player not found!";
        when(refreshTokenService.updateToken(requestDto.token()))
                    .thenThrow(new UsernameNotFoundException(errorMessage));
        mockMvc.perform(post("/api/v1/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorMsg").value(errorMessage));
    }
    
}
