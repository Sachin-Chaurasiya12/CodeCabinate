package com.example.ConnectService.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ConnectService.model.Community;
import com.example.ConnectService.model.privatechat;
import com.example.ConnectService.model.dto.CommunityMessageResponse;
import com.example.ConnectService.model.dto.PrivateMessageRequest;
import com.example.ConnectService.model.dto.PrivateMessageResponse;
import com.example.ConnectService.model.dto.TermRequest;
import com.example.ConnectService.repository.CommuntiyRepo;
import com.example.ConnectService.repository.PrivateChatRepo;
import com.example.ConnectService.service.Interface.IMessageService;

@RestController
@RequestMapping("/Community")
public class MessageController {
    
    private CommuntiyRepo repo;
    private IMessageService service;
    private PrivateChatRepo repo2;
    public MessageController( CommuntiyRepo repo,IMessageService service,PrivateChatRepo repo2){
        this.repo = repo;
        this.service = service;
        this.repo2 = repo2;
    }

    @GetMapping("/messages")
    public ResponseEntity<List<CommunityMessageResponse>> getMessage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));
        Page<Community> message = repo.findByIsActiveTrue(pageable);

        List<CommunityMessageResponse> response = message.stream()
            .map(this::toResponse)
            .collect(Collectors.toList()
        ); 

        return ResponseEntity.ok(response);
    }

    @GetMapping("/privatemessage/{reciptentid}")
    public ResponseEntity<List<PrivateMessageResponse>> getchatmessage(
        @PathVariable Long reciptentid,
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ){

        Long senderid = (Long)authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));

        Page<privatechat> messages = repo2.findByFromWhomAndToWhomOrToWhomAndFromWhomOrderByCreatedAtAsc(
            senderid, reciptentid, senderid, reciptentid, pageable);

        List<PrivateMessageResponse> responses = messages
        .getContent()
        .stream()
        .map(message -> new PrivateMessageResponse(
            message.getFromWhom(),
            message.getToWhom(),
            message.getMessage(),
            message.getCreatedAt()
        )).toList();
        
        return ResponseEntity.ok(responses);
        
    }

    private CommunityMessageResponse toResponse(Community request){
        CommunityMessageResponse response = new CommunityMessageResponse();
        response.setContent(request.getContent());
        response.setId(request.getId());
        response.setSenderUserId(request.getSenderUserId());
        response.setReplyUserId(request.getReplyToMessageId());
        response.setCreatedAt(request.getCreatedAt());

        return response;
    }

    @PostMapping("/terms") 
    public void TermRequest(@RequestBody TermRequest request,Authentication authentication){
        Long userid = (Long)authentication.getPrincipal();
        service.istermschecked(request.getTermreq(), userid);
    }

    @GetMapping("/terms")
    public boolean getTerm(Authentication authentication){
        Long userid = (Long)authentication.getPrincipal();
        return service.getTermsChecked(userid);
    }

}
