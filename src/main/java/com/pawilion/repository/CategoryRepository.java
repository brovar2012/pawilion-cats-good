package com.pawilion.repository;

import com.pawilion.model.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {}
