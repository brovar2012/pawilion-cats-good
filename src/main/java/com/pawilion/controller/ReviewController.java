package com.pawilion.controller;

import com.pawilion.model.Review;
import com.pawilion.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewRepository reviewRepository;

  @GetMapping
  public Flux<Review> getAllReviews() {
    return reviewRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<Review>> getReviewById(@PathVariable("id") Long id) {
    return reviewRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Mono<ResponseEntity<Review>> createReview(@RequestBody Review review) {
    return reviewRepository
        .save(review)
        .map(savedReview -> ResponseEntity.status(HttpStatus.CREATED).body(savedReview));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteReview(@PathVariable("id") Long id) {
    return reviewRepository
        .findById(id)
        .flatMap(
            existingReview ->
                reviewRepository
                    .delete(existingReview)
                    .then(Mono.just(ResponseEntity.noContent().<Void>build())))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
