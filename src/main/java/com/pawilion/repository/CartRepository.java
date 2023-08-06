package com.pawilion.repository;

import com.pawilion.model.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartRepository extends ReactiveCrudRepository<Cart, Long> {}
