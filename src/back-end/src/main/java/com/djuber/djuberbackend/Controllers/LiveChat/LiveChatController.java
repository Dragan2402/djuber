package com.djuber.djuberbackend.Controllers.LiveChat;

import com.djuber.djuberbackend.Application.Services.LiveChat.ILiveChatService;
import com.djuber.djuberbackend.Controllers.LiveChat.Requests.MessageRequest;
import com.djuber.djuberbackend.Controllers.LiveChat.Responses.MessageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Live Chat controller",description = "Provides live chat functions.")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class LiveChatController {

    private final ILiveChatService liveChatService;

    @MessageMapping("chat/{subjectIdentityId}")
    public void sendMessage(@DestinationVariable Long subjectIdentityId, MessageRequest request){
        System.out.println("Message with content: "+request.getContent()+" to chat with subject identity id: "+ subjectIdentityId);
        liveChatService.sendMessage(subjectIdentityId, request);
    }

    @GetMapping(value = "api/getMessages")
    public ResponseEntity<List<MessageResponse>> getMessages(@RequestParam("chatId") Long chatId){
        return new ResponseEntity<>(liveChatService.getMessagesByChatId(chatId), HttpStatus.OK);
    }

    @GetMapping(value = "api/getMessagesByIdentityId")
    public ResponseEntity<List<MessageResponse>> getMessagesByIdentityId(@RequestParam("identityId") Long identityId){
        return new ResponseEntity<>(liveChatService.getMessagesByIdentityId(identityId), HttpStatus.OK);
    }
}
