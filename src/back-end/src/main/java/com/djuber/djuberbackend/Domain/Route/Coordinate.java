package com.djuber.djuberbackend.Domain.Route;

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
@SQLDelete(sql = "UPDATE coordinate SET deleted = true WHERE id = ?")
public class Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "index", nullable = false)
    Integer index;

    @Column(name = "locationName")
    String locationName;

    @Column(name = "lat", nullable = false)
    Double lat;

    @Column(name = "lon", nullable = false)
    Double lon;

    @ManyToOne
    @JoinColumn(name = "routeId", nullable = false)
    Route route;

    @Column(name = "isStop", nullable = false)
    Boolean isStop;
}
