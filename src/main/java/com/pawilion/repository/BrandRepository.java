package com.pawilion.repository;

import com.pawilion.model.Brand;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BrandRepository extends ReactiveCrudRepository<Brand, Long> {}
