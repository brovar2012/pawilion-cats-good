package com.pawilion.integration;

import static org.mockito.Mockito.when;

import com.pawilion.BaseIntegrationTest;
import com.pawilion.model.Customer;
import com.pawilion.repository.CustomerRepository;
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
public class CustomerControllerTests extends BaseIntegrationTest {
  @Autowired private WebTestClient webTestClient;

  @MockBean private CustomerRepository customerRepository;

  @Test
  public void testCreateCustomer() {
    Customer customer = new Customer();
    customer.setName("John Doe");
    customer.setEmail("john.doe@example.com");
    customer.setPhone("1234567890");

    when(customerRepository.save(customer)).thenReturn(Mono.just(customer));

    webTestClient
        .post()
        .uri("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(customer), Customer.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.name")
        .isEqualTo("John Doe")
        .jsonPath("$.email")
        .isEqualTo("john.doe@example.com")
        .jsonPath("$.phone")
        .isEqualTo("1234567890");
  }

  @Test
  public void testGetAllCustomers() {
    Customer customer1 = new Customer();
    customer1.setName("John Doe");
    customer1.setEmail("john.doe@example.com");
    customer1.setPhone("1234567890");

    Customer customer2 = new Customer();
    customer2.setName("Jane Smith");
    customer2.setEmail("jane.smith@example.com");
    customer2.setPhone("9876543210");

    when(customerRepository.findAll()).thenReturn(Flux.just(customer1, customer2));

    webTestClient
        .get()
        .uri("/customers")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$[0].name")
        .isEqualTo("John Doe")
        .jsonPath("$[0].email")
        .isEqualTo("john.doe@example.com")
        .jsonPath("$[0].phone")
        .isEqualTo("1234567890")
        .jsonPath("$[1].name")
        .isEqualTo("Jane Smith")
        .jsonPath("$[1].email")
        .isEqualTo("jane.smith@example.com")
        .jsonPath("$[1].phone")
        .isEqualTo("9876543210");
  }
}
