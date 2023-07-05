package com.pawilion.repository;

import com.pawilion.model.Pet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PetRepository extends ReactiveCrudRepository<Pet, Long> {}
