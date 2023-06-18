package com.pawilion.controller;

import com.pawilion.exception.NotFoundException;
import com.pawilion.model.Customer;
import com.pawilion.model.Pet;
import com.pawilion.repository.CustomerRepository;
import com.pawilion.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {
  private final PetRepository petRepository;

  private final CustomerRepository customerRepository;

  @GetMapping("")
  public Flux<Pet> getAllPets() {
    return petRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<Pet> getPetById(@PathVariable Long id) {
    return petRepository.findById(id);
  }

  @PostMapping("")
  public Mono<Pet> createPet(@RequestBody Pet pet) {
    return petRepository.save(pet);
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<Pet>> updatePet(@PathVariable Long id, @RequestBody Pet pet) {
    return petRepository
        .findById(id)
        .flatMap(
            existingPet -> {
              existingPet.setName(pet.getName());
              existingPet.setSpecies(pet.getSpecies());
              existingPet.setAge(pet.getAge());
              return petRepository.save(existingPet);
            })
        .map(updatedPet -> new ResponseEntity<>(updatedPet, HttpStatus.OK))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deletePet(@PathVariable Long id) {
    return petRepository
        .deleteById(id)
        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/{petId}/link-customer/{customerId}")
  public Mono<ResponseEntity<String>> linkPetToCustomer(
      @PathVariable Long petId, @PathVariable Long customerId) {
    Mono<Customer> customerMono =
        customerRepository
            .findById(customerId)
            .switchIfEmpty(Mono.error(new NotFoundException("Customer not found.")));

    Mono<Pet> petMono =
        petRepository
            .findById(petId)
            .switchIfEmpty(Mono.error(new NotFoundException("Pet not found.")));

    return Mono.zip(customerMono, petMono)
        .flatMap(
            tuple -> {
              Pet pet = tuple.getT2();
              pet.setCustomerId(tuple.getT1().getId());
              return petRepository.save(pet);
            })
        .map(savedPet -> ResponseEntity.ok("Pet linked to the customer."))
        .onErrorResume(
            NotFoundException.class, exception -> Mono.just(ResponseEntity.notFound().build()));
  }
}
