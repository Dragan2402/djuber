package com.djuber.djuberbackend.Domain.Route;

import com.djuber.djuberbackend.Domain.Ride.Reservation;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "route",fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    Set<Coordinates> coordinates = new HashSet<>();

    @Column(name = "deleted", nullable = false)
    Boolean deleted;
}
