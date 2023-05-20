package com.pawilion;

import static org.mockito.Mockito.when;

import com.pawilion.model.Order;
import com.pawilion.repository.OrderRepository;
import java.math.BigDecimal;
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
public class OrderControllerTests extends BaseIntegrationTest {
  @Autowired private WebTestClient webTestClient;

  @MockBean private OrderRepository orderRepository;

  @Test
  public void testCreateOrder() {
    Order order = new Order();
    order.setCustomerId(1L);
    order.setTotalAmount(BigDecimal.valueOf(100.0));

    when(orderRepository.save(order)).thenReturn(Mono.just(order));

    webTestClient
        .post()
        .uri("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(order), Order.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.customerId")
        .isEqualTo(1)
        .jsonPath("$.totalAmount")
        .isEqualTo(100.0);
  }

  @Test
  public void testGetAllOrders() {
    Order order1 = new Order();
    order1.setCustomerId(1L);
    order1.setTotalAmount(BigDecimal.valueOf(100.0));

    Order order2 = new Order();
    order2.setCustomerId(2L);
    order2.setTotalAmount(BigDecimal.valueOf(200.0));

    when(orderRepository.findAll()).thenReturn(Flux.just(order1, order2));

    webTestClient
        .get()
        .uri("/orders")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$[0].customerId")
        .isEqualTo(1)
        .jsonPath("$[0].totalAmount")
        .isEqualTo(100.0)
        .jsonPath("$[1].customerId")
        .isEqualTo(2)
        .jsonPath("$[1].totalAmount")
        .isEqualTo(200.0);
  }
}
