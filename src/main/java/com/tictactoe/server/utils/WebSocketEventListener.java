package com.tictactoe.server.utils;

import com.tictactoe.server.dto.GameConnectionStatusMessageDto;
import com.tictactoe.server.enums.ConnectionStatus;
import com.tictactoe.server.exceptions.GameSessionNotFoundException;
import com.tictactoe.server.repositories.DisconnectedPlayersRepository;
import com.tictactoe.server.security.UserDetailsImpl;
import com.tictactoe.server.services.MessageCacheService;
import com.tictactoe.server.services.WebSocketMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final DisconnectedPlayersRepository disconnectedPlayersRepository;
    private final WebSocketMessagingService webSocketMessagingService;
    private final MessageCacheService messageCacheService;

    @EventListener
    public void handleEvent(SessionSubscribeEvent event){
        Long playerId = ((UserDetailsImpl)((Authentication)event.getUser()).getPrincipal()).getPlayer().getId();
        String subscribeDestination = event.getMessage().getHeaders().get("simpDestination").toString();
        if (subscribeDestination.startsWith("/game/moves/")){
            Long gameId = Long.parseLong(subscribeDestination.substring(12));
            String sessionId = getSessionId(event);
            if (disconnectedPlayersRepository.isDisconnected(gameId,playerId)) {
                var dto = new GameConnectionStatusMessageDto(ConnectionStatus.RECONNECTED,playerId);
                webSocketMessagingService.sendConnectionStatusMessage(dto,gameId);
                disconnectedPlayersRepository.remove(gameId,playerId);
            }
            messageCacheService.addSubscribe(sessionId,gameId);
        }
    }

    @EventListener
    public void handleEvent(SessionDisconnectEvent event){
        if (event.getUser() == null) return;
        Long playerId = ((UserDetailsImpl)((Authentication)event.getUser()).getPrincipal()).getPlayer().getId();
        String sessionId = getSessionId(event);
        try {
            Long gameId = messageCacheService.findGameBySessionId(sessionId);
            disconnectedPlayersRepository.markDisconnected(gameId,playerId);
            GameConnectionStatusMessageDto dto = new GameConnectionStatusMessageDto(ConnectionStatus.DISCONNECTED,playerId);
            webSocketMessagingService.sendConnectionStatusMessage(dto, gameId);
        } catch (GameSessionNotFoundException ignored) {
        }

    }

    private String getSessionId(AbstractSubProtocolEvent event){
        return event.getMessage().getHeaders().get("simpSessionId").toString();
    }
}
