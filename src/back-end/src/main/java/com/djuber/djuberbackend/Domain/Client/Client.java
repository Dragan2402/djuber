package com.djuber.djuberbackend.Domain.Client;

import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Review.Review;
import com.djuber.djuberbackend.Domain.Ride.Reservation;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Route.FavouriteRoute;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE client SET deleted = true WHERE id = ?")
public class Client{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "identityId")
    Identity identity;

    @Column(name = "firstName", nullable = false)
    String firstName;

    @Column(name = "lastName", nullable = false)
    String lastName;

    @Column(name = "city", nullable = false)
    String city;

    @Column(name = "phoneNumber", nullable = false)
    String phoneNumber;

    @Column(name = "verified", nullable = false)
    Boolean verified;

    @Column(name = "balance", nullable = false)
    Double balance;

    @Column(name = "signingType", nullable = false)
    ClientSigningType clientSigningType;

    @ManyToMany(mappedBy = "clients")
    Set<Ride> rides = new HashSet<>();

    @ManyToMany(mappedBy = "clients")
    Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    Set<FavouriteRoute> favouriteRoutes = new HashSet<>();

    @Column(name = "blocked", nullable = false)
    Boolean blocked;

    @Column(name = "inRide", nullable = false)
    Boolean inRide;

    @Column(name = "verificationToken")
    String verificationToken;

    @Column(name = "verificationTokenExpirationDate")
    Date verificationTokenExpirationDate;

    @Column(name = "note", length = 500)
    String note;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return getId().equals(client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
