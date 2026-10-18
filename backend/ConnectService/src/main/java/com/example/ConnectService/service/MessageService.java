package com.example.ConnectService.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.ConnectService.Exception.IllegalArgumentException;
import com.example.ConnectService.Exception.UserNotFoundException;
import com.example.ConnectService.model.Community;
import com.example.ConnectService.model.Terms;
import com.example.ConnectService.model.dto.CommunityMessageResponse;
import com.example.ConnectService.model.dto.SendMessageRequest;
import com.example.ConnectService.repository.CommuntiyRepo;
import com.example.ConnectService.repository.TermsRepo;
import com.example.ConnectService.service.Interface.IMessageService;

@Service
public class MessageService implements IMessageService{

    private final CommuntiyRepo repo;
    private final TermsRepo repo2;
    private final int MAX_MESSAGE_LENGTH = 2000;
    private final int MIN_MESSAGE_LENGTH = 0;
    public MessageService(CommuntiyRepo repo,TermsRepo repo2){
        this.repo = repo;
        this.repo2 = repo2;
    }
    @Override
    public ResponseEntity<CommunityMessageResponse> sendMessage(Long userId, SendMessageRequest request) {
       
        if(userId == null || userId <= 0){
            throw new UserNotFoundException("Invalid User Id");
        }

        vaidateMessageContent(request.getContent());

        if(request.getReplyToMessageId() != null){
            validateReplyToMessage(request.getReplyToMessageId());
        }

        Community message = createMessage(userId, request);
        Community saved = repo.save(message);

        CommunityMessageResponse response = new CommunityMessageResponse();

        response.setId(saved.getId());
        response.setSenderUserId(saved.getSenderUserId());
        response.setContent(saved.getContent());
        response.setReplyUserId(saved.getReplyToMessageId());
        response.setCreatedAt(saved.getCreatedAt());

        return ResponseEntity.ok(response);
        
    }
    public Community createMessage(Long userId,SendMessageRequest request){
        Community com = new Community();
        com.setContent(request.getContent());
        com.setSenderUserId(userId);
        com.setReplyToMessageId(request.getReplyToMessageId());
        com.setCreatedAt(LocalDateTime.now());
        return  com;
    }
    private void vaidateMessageContent(String content){
        if(content == null ||  content.trim().isEmpty()){
            throw new IllegalArgumentException("Message cannot be null");
        }
        if(content.length() < MIN_MESSAGE_LENGTH){
            throw new IllegalArgumentException("Message cannot be null");
        }
        if(content.length() > MAX_MESSAGE_LENGTH){
            throw new IllegalArgumentException(
                String.format("Message cannot be greater then %d character",MAX_MESSAGE_LENGTH)
            );
        }
    }
    private void validateReplyToMessage(Long replyToMessageId) {
        Optional<Community> replyTo = repo.findById(replyToMessageId);
        if (replyTo.isEmpty()) {
            throw new IllegalArgumentException("Message " + replyToMessageId + " not found for reply");
        }
        if (replyTo.get().getDeletedAt() != null) {
            throw new IllegalArgumentException("Cannot reply to deleted message");
        }
    }
     
    public ResponseEntity<Void> deleteMessage(Long MessageId, Long userId){
        Community messsage = repo.findById(MessageId).orElseThrow(
            () -> new IllegalArgumentException("Message Not found") 
        );

        if(!messsage.getSenderUserId().equals(userId)){
            throw new IllegalArgumentException("Cannot delete message for other user");
        }

        messsage.setIsActive(false);
        messsage.setDeletedAt(LocalDateTime.now());
        repo.save(messsage);

        return  ResponseEntity.ok().build();
    }
    @Override 
    public void istermschecked(boolean checked, Long userId){


        if(!repo2.existsByUserId(userId)){
            Terms terms = new Terms(userId, checked);
            repo2.save(terms);
        }else{
            Terms terms = repo2.findByUserId(userId).orElse(null);
            terms.setIstermChecked(checked);
            repo2.save(terms);
        }
        
    }
    @Override 
    public boolean getTermsChecked(Long userId){

       Terms terms = repo2.findByUserId(userId).orElse(null);

       boolean termchecked = terms != null && terms.getIstermChecked();

       return termchecked;
    }
}
