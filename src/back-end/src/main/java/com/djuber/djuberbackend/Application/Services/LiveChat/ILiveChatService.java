package com.djuber.djuberbackend.Application.Services.LiveChat;

import com.djuber.djuberbackend.Controllers.LiveChat.Requests.MessageRequest;
import com.djuber.djuberbackend.Controllers._Common.Responses.ChatResponse;
import com.djuber.djuberbackend.Controllers.LiveChat.Responses.MessageResponse;

import java.util.List;

public interface ILiveChatService {

    void sendMessage(Long subjectIdentityId, MessageRequest request);

    List<ChatResponse> getChats();

    List<MessageResponse> getMessagesByChatId(Long chatId);

    void deleteChatById(Long chatId);

    List<MessageResponse> getMessagesByIdentityId(Long identityId);

    long getNumberOfChats();
}
