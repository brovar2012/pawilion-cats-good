package com.pawilion.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.pawilion.BaseIntegrationTest;
import com.pawilion.TestData;
import com.pawilion.model.Pet;
import com.pawilion.repository.CustomerRepository;
import com.pawilion.repository.PetRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class PetsControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired private WebTestClient webTestClient;

  @Autowired private PetRepository petRepository;

  @Autowired private CustomerRepository customerRepository;

  @AfterEach
  public void cleanup() {
    customerRepository.deleteAll().then(petRepository.deleteAll()).block();
  }

  @Test
  public void testGetPetById() {

    var customer = customerRepository.save(TestData.buildTestCustomer()).block();

    Pet pet =
        petRepository
            .save(
                Pet.builder()
                    .customerId(customer.getId())
                    .age(1)
                    .name("Buddy")
                    .species("dog")
                    .build())
            .block();
    assertNotNull(pet);

    webTestClient
        .get()
        .uri("/pets/{id}", pet.getId())
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody(Pet.class)
        .isEqualTo(pet);
  }

  @Test
  public void testGetAllPets() {
    var customer = customerRepository.save(TestData.buildTestCustomer()).block();

    Pet pet1 =
        petRepository
            .save(
                Pet.builder()
                    .customerId(customer.getId())
                    .age(1)
                    .name("Rex")
                    .species("Dog")
                    .build())
            .block();
    Pet pet2 =
        petRepository
            .save(
                Pet.builder()
                    .customerId(customer.getId())
                    .age(1)
                    .name("Whiskers")
                    .species("Cat")
                    .build())
            .block();

    petRepository.saveAll(List.of(pet1, pet2)).collectList().block();

    webTestClient
        .get()
        .uri("/pets")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$[0].name")
        .isEqualTo("Rex")
        .jsonPath("$[0].species")
        .isEqualTo("Dog")
        .jsonPath("$[1].name")
        .isEqualTo("Whiskers")
        .jsonPath("$[1].species")
        .isEqualTo("Cat");
  }

  @Test
  public void testCreatePet() {
    var customer = customerRepository.save(TestData.buildTestCustomer()).block();
    Pet pet =
        petRepository
            .save(
                Pet.builder()
                    .customerId(customer.getId())
                    .age(2)
                    .name("Dog")
                    .species("Rex")
                    .build())
            .block();

    webTestClient
        .post()
        .uri("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(pet), Pet.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.name")
        .isEqualTo("Dog")
        .jsonPath("$.species")
        .isEqualTo("Rex");
  }

  @Test
  public void testAddPetWithCustomerId() {
    Pet pet = new Pet();
    pet.setName("Fluffy");
    pet.setAge(3);
    pet.setSpecies("Dog");
    pet.setCustomerId(1L); // Set the desired customer ID

    webTestClient
        .post()
        .uri("/pets")
        .bodyValue(pet)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Pet.class)
        .value(
            responsePet -> {
              // Assert the response
              assertNotNull(responsePet.getId());
              assertEquals(pet.getName(), responsePet.getName());
              assertEquals(pet.getAge(), responsePet.getAge());
              assertEquals(pet.getSpecies(), responsePet.getSpecies());
              assertEquals(pet.getCustomerId(), responsePet.getCustomerId());
            });
  }
}
