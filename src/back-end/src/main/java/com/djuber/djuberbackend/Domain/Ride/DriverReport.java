package com.djuber.djuberbackend.Domain.Ride;

import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "driverId", nullable = false)
    Driver driver;

    @OneToOne
    @JoinColumn(name = "clientId", nullable = false)
    Client client;

    @OneToOne
    @JoinColumn(name = "rideId", nullable = false)
    Ride ride;

    @Column(name = "reason", nullable = false)
    String reason;
}
