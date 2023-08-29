package com.pawilion.integration;

import com.pawilion.BaseIntegrationTest;
import com.pawilion.model.User;
import com.pawilion.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class UserControllerTests extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetAllUsers() {
        User user1 = new User(1L, "user1", "user1@example.com");
        User user2 = new User(2L, "user2", "user2@example.com");
        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(Flux.fromIterable(userList));

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .isEqualTo(userList);
    }

    @Test
    public void testGetUserById() {
        User user = new User(1L, "user1", "user1@example.com");

        when(userRepository.findById(1L)).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(user);
    }

    @Test
    public void testCreateUser() {
        User user = new User(null, "newuser", "newuser@example.com");
        User createdUser = new User(1L, "newuser", "newuser@example.com");

        when(userRepository.save(any(User.class))).thenReturn(Mono.just(createdUser));

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .isEqualTo(createdUser);
    }

}
