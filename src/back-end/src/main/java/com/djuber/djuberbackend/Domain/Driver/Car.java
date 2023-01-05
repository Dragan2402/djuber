package com.djuber.djuberbackend.Domain.Driver;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE car SET deleted = true WHERE id = ?")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "carType", nullable = false)
    CarType carType;

    @Column(name = "licensePlate" , unique = true, nullable = false)
    String licensePlate;

//    @OneToOne
//    @JoinColumn(name = "coordinatesId")
//    private Coordinates coordinates;

    @OneToOne(mappedBy = "car")
    Driver driver;

    @Column(name = "x", nullable = false)
    Double x;

    @Column(name = "y", nullable = false)
    Double y;

    @Column(name = "additionalServices", nullable = false)
    @ElementCollection(targetClass = String.class)
    Set<String> additionalServices;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;
}

