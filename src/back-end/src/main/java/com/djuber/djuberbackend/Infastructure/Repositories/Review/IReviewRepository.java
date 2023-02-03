package com.djuber.djuberbackend.Infastructure.Repositories.Review;

import com.djuber.djuberbackend.Domain.Review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review  r where r.client.id = ?1")
    Review findByClientId(Long clientId);

    @Query("select r from Review r where r.client.id=?1 and r.ride.id = ?2")
    Review findByClientIdAndRideId(Long clientId, Long rideId);
}
