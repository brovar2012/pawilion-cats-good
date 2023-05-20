package com.pawilion;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.pawilion.model.Pet;
import com.pawilion.repository.PetRepository;
import java.util.List;
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

  @Test
  public void testGetPetById() {
    Pet pet = petRepository.save(new Pet("Buddy", "dog", 1)).block();

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
    Pet pet1 = new Pet("Dog", "Rex", 1);
    Pet pet2 = new Pet("Cat", "Whiskers", 1);

    petRepository.saveAll(List.of(pet1, pet2)).collectList().block();

    webTestClient
        .get()
        .uri("/pets")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$[0].name")
        .isEqualTo("Dog")
        .jsonPath("$[0].species")
        .isEqualTo("Rex")
        .jsonPath("$[1].name")
        .isEqualTo("Cat")
        .jsonPath("$[1].species")
        .isEqualTo("Whiskers");
  }

  @Test
  public void testCreatePet() {
    Pet pet = new Pet("Dog", "Rex", 2);

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
}
