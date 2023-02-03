package com.djuber.djuberbackend.Domain.Authentication;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE identity SET deleted = true WHERE id = ?")
public class Identity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "userType", nullable = false)
    UserType userType;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Role> roles = new ArrayList<>();

    @Column(name = "passwordResetToken")
    String passwordResetToken;

    @Column(name = "passwordResetTokenExpirationDate")
    Date passwordResetTokenExpirationDate;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;
}
