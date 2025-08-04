package com.tictactoe.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;

@Configuration
public class WebSocketConfig extends WebSocketMessageBrokerConfigurationSupport {

    @Override
    protected void registerStompEndpoints(StompEndpointRegistry registry) {
        // TODO Auto-generated method stub
        registry.addEndpoint("/ws/connect")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    protected void configureClientInboundChannel(ChannelRegistration registration) {
        // TODO Auto-generated method stub
        registration.interceptors(new ChannelInterceptor(){
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                return message;
            }
        });
        super.configureClientInboundChannel(registration);
    }

    @Override
    protected void configureMessageBroker(MessageBrokerRegistry registry) {
        // TODO Auto-generated method stub
        super.configureMessageBroker(registry);
    }
    

}
