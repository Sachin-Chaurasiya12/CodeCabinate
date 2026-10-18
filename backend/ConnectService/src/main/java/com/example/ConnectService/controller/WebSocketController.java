package com.example.ConnectService.controller;

import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.example.ConnectService.Exception.IllegalStateException;
import com.example.ConnectService.model.privatechat;
import com.example.ConnectService.model.dto.CommunityMessageResponse;
import com.example.ConnectService.model.dto.PrivateMessageRequest;
import com.example.ConnectService.model.dto.PrivateMessageResponse;
import com.example.ConnectService.model.dto.SendMessageRequest;
import com.example.ConnectService.repository.PrivateChatRepo;
import com.example.ConnectService.service.Interface.IMessageService;

@Controller 
public class WebSocketController {
    
    private static final Logger logger = 
                Logger.getLogger(WebSocketController.class.getName());

    private final IMessageService service;
    private final SimpMessagingTemplate template;
    private final PrivateChatRepo repo;

    public WebSocketController(IMessageService service,
        SimpMessagingTemplate template,
        PrivateChatRepo repo){
        this.service = service;
        this.template = template;
        this.repo = repo;
    }

    @MessageMapping("/community.send")
    @SendTo("/topic/community")
    public CommunityMessageResponse sendMessage(
        Authentication authentication,SendMessageRequest request
    ){
        Long userId = (Long)authentication.getPrincipal();
        if(userId == null){
            logger.severe("Websocket principal is null");
            throw new IllegalStateException("Authentication not found");
        }

        ResponseEntity<CommunityMessageResponse> response = 
                service.sendMessage(userId, request);

        return response.getBody();
    }

    @MessageMapping("/community.delete")
    @SendTo("/topic/community")
    public Object deleteMessage(Authentication authentication, Long messageId) {

        Long userId = (Long)authentication.getPrincipal();
        if(userId == null){
            logger.severe("Websocket principal is null");
            throw new IllegalStateException("Authentication not found");
        }

        service.deleteMessage(messageId, userId);

        return new MessageDeletedEvent(messageId);
    }
    @MessageMapping("/private.send")
    public void sendmessage(Authentication authentication, PrivateMessageRequest request){

        Long sender = (Long)authentication.getPrincipal();
        if(sender == null){
            logger.severe("Websocket principal is null");
            throw new IllegalStateException("Authentication not found");
        }

        privatechat chat = new privatechat(sender, request.getRecipientId(), request.getContent());
        chat = repo.save(chat);

        PrivateMessageResponse response = new PrivateMessageResponse(
            chat.getFromWhom(),
            chat.getToWhom(),
            chat.getMessage(),
            chat.getCreatedAt()
        );

        template.convertAndSendToUser(
        request.getRecipientId().toString(),
        "/queue/messages", 
        response);
        
    }

    @MessageExceptionHandler
    public void handleException(Exception ex) {
        logger.severe("WebSocket error: " + ex.getMessage());
        template.convertAndSend(
            "/topic/errors",
            new ErrorPayload("error", ex.getMessage())
        );
    }
    
}
class MessageDeletedEvent {
    public String type = "deleted";
    public Long messageId;
    public MessageDeletedEvent(Long messageId) { this.messageId = messageId; }
}
class ErrorPayload {
    public String code;
    public String message;
    public ErrorPayload(String code, String message) {
        this.code = code;
        this.message = message;
    }
}