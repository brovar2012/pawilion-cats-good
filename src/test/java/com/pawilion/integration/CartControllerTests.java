package com.pawilion.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pawilion.BaseIntegrationTest;
import com.pawilion.model.Cart;
import com.pawilion.repository.CartRepository;
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
public class CartControllerTests extends BaseIntegrationTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private CartRepository cartRepository;

  @Test
  public void testGetAllCarts() {
    Cart cart1 = new Cart(1L, 1L, 1L, 2);
    Cart cart2 = new Cart(2L, 1L, 2L, 3);
    List<Cart> cartList = Arrays.asList(cart1, cart2);

    when(cartRepository.findAll()).thenReturn(Flux.fromIterable(cartList));

    webTestClient
        .get()
        .uri("/carts")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Cart.class)
        .isEqualTo(cartList);
  }

  @Test
  public void testGetCartById() {
    Cart cart = new Cart(1L, 1L, 1L, 2);

    when(cartRepository.findById(1L)).thenReturn(Mono.just(cart));

    webTestClient
        .get()
        .uri("/carts/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Cart.class)
        .isEqualTo(cart);
  }

  @Test
  public void testCreateCart() {
    Cart cart = new Cart(null, 1L, 1L, 2);
    Cart createdCart = new Cart(1L, 1L, 1L, 2);

    when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(createdCart));

    webTestClient
        .post()
        .uri("/carts")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(cart)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Cart.class)
        .isEqualTo(createdCart);
  }
}
