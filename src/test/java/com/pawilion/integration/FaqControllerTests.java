package com.pawilion.integration;

import com.pawilion.BaseIntegrationTest;
import com.pawilion.model.Faq;
import com.pawilion.repository.FaqRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class FaqControllerTests extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FaqRepository faqRepository;

    @AfterEach
    private void clean(){
        faqRepository.deleteAll().block();
    }

    @Test
    public void testGetAllFaqs() {
        Faq faq1 = Faq.builder()
                .question("Question 1")
                .answer("Answer 1").build();
        Faq faq2 = Faq.builder()
                .question("Question 2")
                .answer("Answer 2").build();
        var faqList = List.of(faq1, faq2);

        faqRepository.saveAll(faqList).collectList().block();

        webTestClient.get()
                .uri("/faqs")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Faq.class)
                .isEqualTo(faqList);
    }

    @Test
    public void testGetFaqById() {
        Faq faq = Faq.builder()
                .question("Question")
                .answer("Answer").build();

       faqRepository.save(faq).block();

        webTestClient.get()
                .uri("/faqs/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Faq.class)
                .isEqualTo(faq);
    }

    @Test
    public void testCreateFaq() {
        Faq faq = Faq.builder()
                .question("New Question")
                .answer("New Answer").build();
        Faq createdFaq = Faq.builder()
                .question("New Question")
                .answer("New Answer").build();

        faqRepository.save(faq).block();

        webTestClient.post()
                .uri("/faqs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(faq)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Faq.class)
                .isEqualTo(createdFaq);
    }

}
