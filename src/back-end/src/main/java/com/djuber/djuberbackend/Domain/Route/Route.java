package com.djuber.djuberbackend.Domain.Route;

import com.djuber.djuberbackend.Domain.Ride.Reservation;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE route SET deleted = true WHERE id = ?")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(mappedBy = "route")
    Ride ride;

    @OneToOne(mappedBy = "route")
    Reservation reservation;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    List<Coordinate> coordinates = new ArrayList<>();

    @Column(name = "stopNames", nullable = false)
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    List<String> stopNames;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;

    public Coordinate getStartCoordinate() {
        Coordinate firstCoordinate = coordinates.get(0);
        if (firstCoordinate.getIndex() != 0) {
            for (Coordinate c : coordinates) {
                if (c.getIndex() == 0) {
                    return c;
                }
            }
        }
        return firstCoordinate;
    }
}
