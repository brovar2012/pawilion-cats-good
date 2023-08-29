package com.pawilion.controller;

import com.pawilion.model.Brand;
import com.pawilion.repository.BrandRepository;
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
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

  private final BrandRepository brandRepository;

  @GetMapping
  public Flux<Brand> getAllBrands() {
    return brandRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<Brand>> getBrandById(@PathVariable("id") Long id) {
    return brandRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Mono<ResponseEntity<Brand>> createBrand(@RequestBody Brand brand) {
    return brandRepository
        .save(brand)
        .map(savedBrand -> ResponseEntity.status(HttpStatus.CREATED).body(savedBrand));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteBrand(@PathVariable("id") Long id) {
    return brandRepository
        .findById(id)
        .flatMap(
            existingBrand ->
                brandRepository
                    .delete(existingBrand)
                    .then(Mono.just(ResponseEntity.noContent().<Void>build())))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
