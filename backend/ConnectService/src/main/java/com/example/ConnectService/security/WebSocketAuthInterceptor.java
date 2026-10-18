package com.example.ConnectService.security;

import java.util.List;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.ConnectService.service.JwtService;

import jakarta.annotation.Nullable;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor{

    private final JwtService jwtService;

    public WebSocketAuthInterceptor(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
public @Nullable Message<?> preSend(
        Message<?> message,
        MessageChannel channel) {

    StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(
                    message,
                    StompHeaderAccessor.class
            );

    if (accessor == null) {
        return message;
    }

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {

        try {

            String authHeader =
                    accessor.getFirstNativeHeader("Authorization");

            System.out.println(
                    "Authorization header present: "
                    + (authHeader != null)
            );

            if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

                throw new IllegalArgumentException(
                        "Invalid Authorization Header"
                );
            }

            String token = authHeader.substring(7);

            boolean valid = jwtService.ValidateToken(token);

            if (!valid) {
                throw new IllegalArgumentException(
                        "Invalid JWT token"
                );
            }

            Long userId = jwtService.extractUserId(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            List.of(
                                new SimpleGrantedAuthority("USER_ROLE")
                            )
                    );

            accessor.setUser(authentication);

            accessor.setLeaveMutable(true);

            System.out.println(
                    "STOMP authentication successful. userId="
                    + userId
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw e;
        }
    }

    return message;
}
    
}