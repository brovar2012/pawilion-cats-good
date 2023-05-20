package com.pawilion.controller;

import com.pawilion.model.Pet;
import com.pawilion.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PetController {
  @Autowired private PetRepository petRepository;

  @GetMapping("")
  public Flux<Pet> getAllPets() {
    return petRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<Pet> getPetById(@PathVariable Integer id) {
    return petRepository.findById(id);
  }

  @PostMapping("")
  public Mono<Pet> createPet(@RequestBody Pet pet) {
    return petRepository.save(pet);
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<Pet>> updatePet(@PathVariable Integer id, @RequestBody Pet pet) {
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
  public Mono<ResponseEntity<Void>> deletePet(@PathVariable Integer id) {
    return petRepository
        .deleteById(id)
        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
