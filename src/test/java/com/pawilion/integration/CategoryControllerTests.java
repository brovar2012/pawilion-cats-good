package com.pawilion.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pawilion.BaseIntegrationTest;
import com.pawilion.model.Category;
import com.pawilion.repository.CategoryRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class CategoryControllerTests extends BaseIntegrationTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private CategoryRepository categoryRepository;

  @Test
  public void testGetAllCategories() {
    Category category1 = new Category(1L, "Category 1", "Description 1");
    Category category2 = new Category(2L, "Category 2", "Description 2");
    List<Category> categoryList = Arrays.asList(category1, category2);

    when(categoryRepository.findAll()).thenReturn(Flux.fromIterable(categoryList));

    webTestClient
        .get()
        .uri("/categories")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Category.class)
        .isEqualTo(categoryList);
  }

  @Test
  public void testGetCategoryById() {
    Category category = new Category(1L, "Category 1", "Description 1");

    when(categoryRepository.findById(1L)).thenReturn(Mono.just(category));

    webTestClient
        .get()
        .uri("/categories/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Category.class)
        .isEqualTo(category);
  }

  @Test
  public void testCreateCategory() {
    Category category = new Category(null, "New Category", "New Description");
    Category createdCategory = new Category(1L, "New Category", "New Description");

    when(categoryRepository.save(any(Category.class))).thenReturn(Mono.just(createdCategory));

    webTestClient
        .post()
        .uri("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(category)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Category.class)
        .isEqualTo(createdCategory);
  }
}
