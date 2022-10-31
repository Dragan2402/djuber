package com.djuber.djuberbackend.Domain.Client;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE client SET deleted = true WHERE id = ?")
public class Client {

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

    @Column(name = "verified", nullable = false)
    private Boolean verified;

    @ManyToMany(mappedBy = "clients")
    private Set<Ride> rides = new HashSet<>();

    @Column(name = "blocked", nullable = false)
    private Boolean blocked;

    @Column(name = "inRide", nullable = false)
    private Boolean inRide;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;


}
