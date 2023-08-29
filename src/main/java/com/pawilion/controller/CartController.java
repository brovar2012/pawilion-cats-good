package com.pawilion.controller;

import com.pawilion.model.Cart;
import com.pawilion.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartRepository cartRepository;

  @GetMapping
  public Flux<Cart> getAllCarts() {
    return cartRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<Cart>> getCartById(@PathVariable("id") Long id) {
    return cartRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Mono<ResponseEntity<Cart>> createCart(@RequestBody Cart cart) {
    return cartRepository
        .save(cart)
        .map(savedCart -> ResponseEntity.status(HttpStatus.CREATED).body(savedCart));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteCart(@PathVariable("id") Long id) {
    return cartRepository
        .findById(id)
        .flatMap(
            existingCart ->
                cartRepository
                    .delete(existingCart)
                    .then(Mono.just(ResponseEntity.noContent().<Void>build())))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
