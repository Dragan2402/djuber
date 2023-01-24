package com.djuber.djuberbackend.Domain.Ride;

import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Review.Review;
import com.djuber.djuberbackend.Domain.Route.Route;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
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
@SQLDelete(sql = "UPDATE ride SET deleted = true WHERE id = ?")
public class Ride implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "driverId", nullable = false)
    Driver driver;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "clients_rides",joinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"))
    Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "ride",fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    Set<Review> reviews = new HashSet<>();

    @Column(name = "rideType", nullable = false)
    RideType rideType;

    @Column(name = "start")
    OffsetDateTime start;

    @Column(name = "finish")
    OffsetDateTime finish;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_id", nullable = false)
    Route route;

    @Column(name = "price", nullable = false)
    Double price;

    @Column(name = "rideStatus", nullable = false)
    RideStatus rideStatus;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;
}
