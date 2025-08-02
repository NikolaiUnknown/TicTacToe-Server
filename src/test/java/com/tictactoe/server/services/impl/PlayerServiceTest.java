package com.tictactoe.server.services.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tictactoe.server.exceptions.NicknameIsUsedException;
import com.tictactoe.server.models.Player;
import com.tictactoe.server.repositories.PlayerRepository;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PlayerServiceImpl playerServiceImpl;

    @Test
    void testSuccessfulLoadPlayerById() {
        Player player = new Player();
        when(playerRepository.findById(0L)).thenReturn(Optional.of(player));
        assertEquals(player,playerServiceImpl.loadPlayerById(0L));
    }

    @Test
    void testLoadNonExistPlayerById() {
        assertThrows(UsernameNotFoundException.class,()-> playerServiceImpl.loadPlayerById(0L));
    }

    @Test
    void testSuccessfulRegisterNewPlayer() {
        Player player = new Player();
        player.setNickname("nickname");
        assertDoesNotThrow(() -> playerServiceImpl.registerNewPlayer(player));
        verify(playerRepository,times(1)).save(player);
    }

    @Test
    void testRegisterAlreadyExistPlayer() {
        Player player = new Player();
        player.setNickname("nickname");
        when(playerRepository.findPlayerByNickname(player.getNickname())).thenReturn(Optional.of(player));
        assertThrows(NicknameIsUsedException.class,() -> playerServiceImpl.registerNewPlayer(player));
    }
}
