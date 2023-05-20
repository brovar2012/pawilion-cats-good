package com.pawilion;

import static org.assertj.core.api.Assertions.assertThat;

import com.pawilion.model.Pet;
import com.pawilion.repository.PetRepository;
import org.junit.jupiter.api.Disabled;
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
public class PetsControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired private WebTestClient webTestClient;

  @Autowired private PetRepository petRepository;

  @Disabled
  @Test
  public void testGetAllPets() {
    webTestClient
        .get()
        .uri("/pets")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Pet.class)
        .hasSize(2);
  }

  @Test
  public void testGetPetById() {
    Pet pet = petRepository.save(new Pet("Buddy", "dog", 1)).block();
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

  @Disabled
  @Test
  public void testCreatePet() {
    Pet pet = new Pet("Luna", "cat", 1);
    webTestClient
        .post()
        .uri("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(pet)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectHeader()
        .valueEquals("location", "/api/pets/3")
        .expectBody(Pet.class)
        .consumeWith(result -> assertThat(result.getResponseBody()).isEqualTo(pet));
  }
}
