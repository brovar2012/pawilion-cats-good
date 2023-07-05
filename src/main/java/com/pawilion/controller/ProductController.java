package com.pawilion.controller;

import com.pawilion.model.Product;
import com.pawilion.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductRepository productRepository;

  @GetMapping
  public Flux<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<Product> getProductById(@PathVariable("id") Long id) {
    return productRepository.findById(id);
  }

  @PostMapping
  public Mono<Product> createProduct(@RequestBody Product product) {
    return productRepository.save(product);
  }

  @PutMapping("/{id}")
  public Mono<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
    return productRepository
        .findById(id)
        .flatMap(
            entity -> {
              product.setId(entity.getId());
              return productRepository.save(product);
            });
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteProduct(@PathVariable("id") Long id) {
    return productRepository.deleteById(id);
  }
}
