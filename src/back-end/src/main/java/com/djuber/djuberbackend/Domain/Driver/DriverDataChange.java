package com.djuber.djuberbackend.Domain.Driver;

import lombok.*;
import lombok.experimental.FieldDefaults;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverDataChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "driverId", unique = true)
    Driver driver;

    @Column(name = "firstName", nullable = false)
    String firstName;

    @Column(name = "lastName", nullable = false)
    String lastName;

    @Column(name = "city", nullable = false)
    String city;

    @Column(name = "phoneNumber", nullable = false)
    String phoneNumber;

    @Column(name = "carType", nullable = false)
    CarType carType;

    @Column(name = "licensePlate" , nullable = false)
    String licensePlate;

    @Column(name = "additionalServices", nullable = false)
    @ElementCollection(targetClass = String.class ,fetch = FetchType.EAGER)
    Set<String> additionalServices;
}
