package com.example.ConnectService.service.Interface;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.ConnectService.model.Community;
import com.example.ConnectService.model.dto.CommunityMessageResponse;
import com.example.ConnectService.model.dto.SendMessageRequest;

@Service
public interface IMessageService {
    
    ResponseEntity<CommunityMessageResponse> sendMessage(Long userId, 
        SendMessageRequest request);
    public Community createMessage(Long userId,SendMessageRequest request);
    public ResponseEntity<Void> deleteMessage(Long MessageId, Long userId);
    void istermschecked(boolean checked, Long userId);
    boolean getTermsChecked(Long userId);
    
}
