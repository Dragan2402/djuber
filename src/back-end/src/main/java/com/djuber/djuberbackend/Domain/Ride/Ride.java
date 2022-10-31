package com.djuber.djuberbackend.Domain.Ride;

import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE ride SET deleted = true WHERE id = ?")
public class Ride implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driverId", nullable = false)
    private Driver driver;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "clients_rides",joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"))
    private Set<Client> clients = new HashSet<>();

    @Column(name = "rideType", nullable = false)
    private RideType rideType;

    @Column(name = "start")
    private OffsetDateTime start;

    @Column(name = "finish")
    private OffsetDateTime finish;

//    //Ruta TODO
//
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "rideStatus", nullable = false)
    private RideStatus rideStatus;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
