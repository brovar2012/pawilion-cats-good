package com.pawilion.repository;

import com.pawilion.model.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReviewRepository extends ReactiveCrudRepository<Review, Long> {}
