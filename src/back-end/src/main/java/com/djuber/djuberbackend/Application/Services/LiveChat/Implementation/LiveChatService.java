package com.djuber.djuberbackend.Application.Services.LiveChat.Implementation;

import com.djuber.djuberbackend.Application.Services.LiveChat.ILiveChatService;
import com.djuber.djuberbackend.Application.Services.LiveChat.Results.MessageResult;
import com.djuber.djuberbackend.Controllers.LiveChat.Requests.MessageRequest;
import com.djuber.djuberbackend.Controllers._Common.Responses.ChatResponse;
import com.djuber.djuberbackend.Controllers.LiveChat.Responses.MessageResponse;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Authentication.UserType;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.LiveChat.Chat;
import com.djuber.djuberbackend.Domain.LiveChat.Message;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.AdminCreatingChatException;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.ChatNotFoundException;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.UserNotFoundException;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.LiveChat.IChatRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.LiveChat.IMessageRepository;
import com.djuber.djuberbackend.Infastructure.Util.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LiveChatService implements ILiveChatService {

    final IChatRepository chatRepository;

    final IIdentityRepository identityRepository;

    final IMessageRepository messageRepository;

    final SimpMessagingTemplate simpMessagingTemplate;

    final IClientRepository clientRepository;

    final IDriverRepository driverRepository;

    final MediaService mediaService;

    @Override
    public void sendMessage(Long subjectIdentityId, MessageRequest request) {
        Chat chat = getOrCreateChat(subjectIdentityId, request.getFromAdmin());
        Message message = new Message();
        message.setDeleted(false);
        message.setSenderName(request.getSenderFirstName()+" "+request.getSenderLastName());
        message.setContent(request.getContent());
        message.setChat(chat);
        message.setTime(new Timestamp(System.currentTimeMillis()));
        message.setFromAdmin(request.getFromAdmin());
        Message messageSaved = messageRepository.save(message);

        simpMessagingTemplate.convertAndSend("/topic/messages/"+subjectIdentityId, new MessageResult(messageSaved));
    }

    @Override
    public List<ChatResponse> getChats() {
        List<Chat> chats = chatRepository.findAll();
        List<ChatResponse> chatResponses = new ArrayList<>();
        for (Chat chat : chats){
            ChatResponse chatResponse = new ChatResponse();
            setUserFirstAndLastNameAndPicture(chatResponse, chat.getSubjectIdentity());
            chatResponse.setId(chat.getId());
            chatResponse.setSubjectIdentityId(chat.getSubjectIdentity().getId());
            chatResponses.add(chatResponse);
        }
        return chatResponses;
    }

    @Override
    public List<MessageResponse> getMessagesByChatId(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if(chat == null){
            throw new ChatNotFoundException("Chat with provided id does not exist.");
        }
        List<Message> messages = messageRepository.findAllByChatId(chatId);
        List<MessageResponse> messageResponses = new ArrayList<>();
        for(Message message : messages){
            messageResponses.add(new MessageResponse(message));
        }
        return messageResponses;
    }

    @Override
    public void deleteChatById(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if(chat == null){
            throw new ChatNotFoundException("Chat with provided id does not exist.");
        }
        chatRepository.delete(chat);
        simpMessagingTemplate.convertAndSend("/topic/messages/","");
    }

    @Override
    public List<MessageResponse> getMessagesByIdentityId(Long identityId) {
        Chat chat = getOrCreateChat(identityId, false);
        List<Message> messages = messageRepository.findAllByChatId(chat.getId());
        List<MessageResponse> messageResponses = new ArrayList<>();
        for(Message message : messages){
            messageResponses.add(new MessageResponse(message));
        }
        return messageResponses;
    }

    @Override
    public long getNumberOfChats() {
        return this.chatRepository.count();
    }

    private Chat getOrCreateChat(Long subjectIdentityId, boolean fromAdmin){
        Identity identity = identityRepository.findById(subjectIdentityId).orElse(null);
        if(identity == null){
            throw new UserNotFoundException("User you are trying to send a message to an user that does not exist.");
        }
        Chat chat = chatRepository.findBySubjectIdentityId(identity.getId());
        if(chat != null){
            return chat;
        }
        if(fromAdmin){
            throw new AdminCreatingChatException("Admin can not create the chat");
        }
        System.out.println("Creating new chat.");
        Chat newChat = new Chat();
        newChat.setDeleted(false);
        newChat.setSubjectIdentity(identity);
        return chatRepository.save(newChat);
    }

    private void setUserFirstAndLastNameAndPicture(ChatResponse chatResponse, Identity identity){
        switch (identity.getUserType()){
            case ADMIN -> {
                chatResponse.setSubjectFirstName("Admin");
                chatResponse.setSubjectLastName("Admin");
                chatResponse.setSubjectPicture("");
            }
            case CLIENT -> {
                Client client = clientRepository.findByIdentityId(identity.getId());
                chatResponse.setSubjectFirstName(client.getFirstName());
                chatResponse.setSubjectLastName(client.getLastName());
                chatResponse.setSubjectPicture(mediaService.readUserPictureAsBase64String(client.getId(), UserType.CLIENT));
            }
            case DRIVER -> {
                Driver driver = driverRepository.findByIdentityId(identity.getId());
                chatResponse.setSubjectFirstName(driver.getFirstName());
                chatResponse.setSubjectLastName(driver.getLastName());
                chatResponse.setSubjectPicture(mediaService.readUserPictureAsBase64String(driver.getId(),UserType.DRIVER));
            }
            default -> {
                chatResponse.setSubjectFirstName("Unknown");
                chatResponse.setSubjectLastName("User");
                chatResponse.setSubjectPicture("");
            }
        }
    }
}
