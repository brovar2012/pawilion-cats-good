package com.pawilion.controller;

import com.pawilion.model.Category;
import com.pawilion.repository.CategoryRepository;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryRepository categoryRepository;

  @GetMapping
  public Flux<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<Category>> getCategoryById(@PathVariable("id") Long id) {
    return categoryRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Mono<ResponseEntity<Category>> createCategory(@RequestBody Category category) {
    return categoryRepository
        .save(category)
        .map(savedCategory -> ResponseEntity.status(HttpStatus.CREATED).body(savedCategory));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteCategory(@PathVariable("id") Long id) {
    return categoryRepository
        .findById(id)
        .flatMap(
            existingCategory ->
                categoryRepository
                    .delete(existingCategory)
                    .then(Mono.just(ResponseEntity.noContent().<Void>build())))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
