package com.djuber.djuberbackend.Domain.Driver;

import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Review.Review;
import com.djuber.djuberbackend.Domain.Ride.Reservation;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE driver SET deleted = true WHERE id = ?")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "identityId")
    Identity identity;

    @Column(name = "firstName", nullable = false)
    String firstName;

    @Column(name = "lastName", nullable = false)
    String lastName;

    @Column(name = "city", nullable = false)
    String city;

    @Column(name = "phoneNumber", nullable = false)
    String phoneNumber;

    @Column(name = "active", nullable = false)
    Boolean active;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Ride> rides = new HashSet<>();

    @OneToMany(mappedBy = "driver",fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    Set<Review> reviews = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carId")
    Car car;

    @Column(name = "lastActivationTime")
    OffsetDateTime lastActivationTime;

    @Column(name = "blocked", nullable = false)
    Boolean blocked;

    @Column(name = "inRide", nullable = false)
    Boolean inRide;

    @Column(name = "note", length = 500)
    String note;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;
}
