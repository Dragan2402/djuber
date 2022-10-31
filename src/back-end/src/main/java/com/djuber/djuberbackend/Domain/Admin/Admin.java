package com.djuber.djuberbackend.Domain.Admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE admin SET deleted = true WHERE id = ?")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "picture", nullable = false)
    private String picture;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    public Admin(Long id, String email, String firstName, String lastName, String password, String city, String phoneNumber, String picture, Boolean deleted) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
        this.deleted = deleted;
    }
}
