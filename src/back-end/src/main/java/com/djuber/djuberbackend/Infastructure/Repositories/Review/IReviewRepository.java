package com.djuber.djuberbackend.Infastructure.Repositories.Review;

import com.djuber.djuberbackend.Domain.Review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReviewRepository extends JpaRepository<Review, Long> {
}
