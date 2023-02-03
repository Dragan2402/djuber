package com.djuber.djuberbackend.Domain.LiveChat;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE message SET deleted = true WHERE id = ?")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "chatId", nullable = false)
    Chat chat;

    @Column(name = "senderName", nullable = false)
    String senderName;

    @Column(name = "content", nullable = false)
    String content;

    @Column(name = "time", nullable = false)
    Timestamp time;

    @Column(name = "fromAdmin", nullable = false)
    Boolean fromAdmin;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;
}
