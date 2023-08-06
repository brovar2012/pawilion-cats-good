package com.pawilion.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pawilion.BaseIntegrationTest;
import com.pawilion.model.Brand;
import com.pawilion.repository.BrandRepository;
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
public class BrandControllerTests extends BaseIntegrationTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private BrandRepository brandRepository;

  @Test
  public void testGetAllBrands() {
    Brand brand1 = new Brand(1L, "Brand 1", "Description 1");
    Brand brand2 = new Brand(2L, "Brand 2", "Description 2");
    List<Brand> brandList = Arrays.asList(brand1, brand2);

    when(brandRepository.findAll()).thenReturn(Flux.fromIterable(brandList));

    webTestClient
        .get()
        .uri("/brands")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Brand.class)
        .isEqualTo(brandList);
  }

  @Test
  public void testGetBrandById() {
    Brand brand = new Brand(1L, "Brand 1", "Description 1");

    when(brandRepository.findById(1L)).thenReturn(Mono.just(brand));

    webTestClient
        .get()
        .uri("/brands/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Brand.class)
        .isEqualTo(brand);
  }

  @Test
  public void testCreateBrand() {
    Brand brand = new Brand(null, "New Brand", "New Description");
    Brand createdBrand = new Brand(1L, "New Brand", "New Description");

    when(brandRepository.save(any(Brand.class))).thenReturn(Mono.just(createdBrand));

    webTestClient
        .post()
        .uri("/brands")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(brand)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Brand.class)
        .isEqualTo(createdBrand);
  }
}
