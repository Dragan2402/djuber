package com.djuber.djuberbackend.Domain.Driver;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE driver SET deleted = true WHERE id = ?")
public class Driver {

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

    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Ride> rides = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "durationActive", nullable = false)
    private Duration durationActive;

    @Column(name = "blocked", nullable = false)
    private Boolean blocked;

    @Column(name = "inRide", nullable = false)
    private Boolean inRide;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
