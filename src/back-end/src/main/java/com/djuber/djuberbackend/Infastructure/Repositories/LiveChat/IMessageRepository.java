package com.djuber.djuberbackend.Infastructure.Repositories.LiveChat;

import com.djuber.djuberbackend.Domain.LiveChat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.chat.id = ?1")
    List<Message> findAllByChatId(Long chatId);
}
