package com.djuber.djuberbackend.Infastructure.Repositories.LiveChat;

import com.djuber.djuberbackend.Domain.LiveChat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c where c.subjectIdentity.id = ?1 and c.deleted=false")
    Chat findBySubjectIdentityId(Long subjectIdentityId);
}
