package com.djuber.djuberbackend.Domain.Ride;


import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Route.Route;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE reservation SET deleted = true WHERE id = ?")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "rideType", nullable = false)
    RideType rideType;

    @Column(name = "car_type")
    CarType carType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "clients_reservations",joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"))
    Set<Client> clients = new HashSet<>();

    @Column(name = "requested_services")
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> requestedServices;

    @Column(name = "start")
    OffsetDateTime start;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_id", nullable = false)
    Route route;

    @Column(name = "price", nullable = false)
    Double price;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;
}
