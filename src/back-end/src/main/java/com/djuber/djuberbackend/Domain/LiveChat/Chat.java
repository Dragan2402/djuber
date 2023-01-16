package com.djuber.djuberbackend.Domain.LiveChat;

import com.djuber.djuberbackend.Domain.Authentication.Identity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE chat SET deleted = true WHERE id = ?")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "subjectIdentityId")
    Identity subjectIdentity;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    Set<Message> messages = new HashSet<>();

    @Column(name = "deleted", nullable = false)
    Boolean deleted;
}
