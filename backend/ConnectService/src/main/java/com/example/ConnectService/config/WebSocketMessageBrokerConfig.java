package com.example.ConnectService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.example.ConnectService.security.WebSocketAuthInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer{

    @Autowired
    private WebSocketAuthInterceptor interceptor;

    @Value("${stomp.web.allowed-origins}")
    private String allowdedorigins;
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/topic","/queue");
        registry.setUserDestinationPrefix("/user");
        registry.setApplicationDestinationPrefixes("/app");

    }   

    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").
            setAllowedOriginPatterns(allowdedorigins).
            withSockJS();
    }

    
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(interceptor);
    }

    
    
}