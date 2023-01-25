package com.djuber.djuberbackend.Domain.Route;

import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavouriteRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "favouriteRoute", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<Coordinate> coordinates = new ArrayList<>();

    @Column(name = "stopNames", nullable = false)
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    List<String> stopNames;

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

    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false)
    Client client;
}
