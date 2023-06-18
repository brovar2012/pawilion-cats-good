package com.pawilion.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.pawilion.BaseIntegrationTest;
import com.pawilion.controller.ProductController;
import com.pawilion.model.Product;
import com.pawilion.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class ProductControllerTests extends BaseIntegrationTest {

  @Autowired private ProductRepository productRepository;

  @Test
  public void testGetAllProducts() {
    Product product1 = new Product("Product 1", "Description 1", 10.0);
    Product product2 = new Product("Product 2", "Description 2", 20.0);
    List<Product> productList = Arrays.asList(product1, product2);

    productRepository.saveAll(productList).blockLast();

    WebTestClient webTestClient =
        WebTestClient.bindToController(new ProductController(productRepository)).build();

    webTestClient
        .get()
        .uri("/products")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Product.class)
        .contains(product1, product2);
  }

  @Test
  public void testGetProductById() {
    Product product = new Product("Product 1", "Description 1", 10.0);

    productRepository.save(product).block();

    WebTestClient webTestClient =
        WebTestClient.bindToController(new ProductController(productRepository)).build();

    webTestClient
        .get()
        .uri("/products/{id}", product.getId())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Product.class)
        .isEqualTo(product);
  }

  @Test
  public void testCreateProduct() {
    Product product = new Product("New Product", "New Description", 15.0);

    WebTestClient webTestClient =
        WebTestClient.bindToController(new ProductController(productRepository)).build();

    webTestClient
        .post()
        .uri("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(product)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Product.class)
        .value(
            returnedProduct -> {
              assertThat(returnedProduct.getName()).isEqualTo(product.getName());
              assertThat(returnedProduct.getDescription()).isEqualTo(product.getDescription());
              assertThat(returnedProduct.getPrice()).isEqualTo(product.getPrice());
              assertThat(returnedProduct.getId()).isNotNull();
            });
  }
}
