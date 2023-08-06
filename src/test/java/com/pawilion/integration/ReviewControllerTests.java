package com.pawilion.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pawilion.BaseIntegrationTest;
import com.pawilion.model.Review;
import com.pawilion.repository.ReviewRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class ReviewControllerTests extends BaseIntegrationTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private ReviewRepository reviewRepository;

  @Test
  public void testGetAllReviews() {
    Review review1 = new Review(1L, 1L, "Good product", 5);
    Review review2 = new Review(2L, 1L, "Not bad", 4);
    List<Review> reviewList = Arrays.asList(review1, review2);

    when(reviewRepository.findAll()).thenReturn(Flux.fromIterable(reviewList));

    webTestClient
        .get()
        .uri("/reviews")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Review.class)
        .isEqualTo(reviewList);
  }

  @Test
  public void testGetReviewById() {
    Review review = new Review(1L, 1L, "Good product", 5);

    when(reviewRepository.findById(1L)).thenReturn(Mono.just(review));

    webTestClient
        .get()
        .uri("/reviews/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Review.class)
        .isEqualTo(review);
  }

  @Test
  public void testCreateReview() {
    Review review = new Review(null, 1L, "New review", 3);
    Review createdReview = new Review(1L, 1L, "New review", 3);

    when(reviewRepository.save(any(Review.class))).thenReturn(Mono.just(createdReview));

    webTestClient
        .post()
        .uri("/reviews")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(review)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Review.class)
        .isEqualTo(createdReview);
  }
}
