package com.djuber.djuberbackend.Domain.Review;

import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE review SET deleted = true WHERE id = ?")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "driverId", nullable = false)
    Driver driver;

    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false)
    Client client;

    @ManyToOne
    @JoinColumn(name = "rideId", nullable = false)
    Ride ride;

    @Column(name = "rating", nullable = false)
    Double rating;

    @Column(name = "comment")
    String comment;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;
}
