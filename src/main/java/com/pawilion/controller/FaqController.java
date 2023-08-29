package com.pawilion.controller;

import com.pawilion.model.Faq;
import com.pawilion.repository.FaqRepository;
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
@RequestMapping("/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqRepository faqRepository;


    @GetMapping
    public Flux<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Faq>> getFaqById(@PathVariable("id") Long id) {
        return faqRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Faq>> createFaq(@RequestBody Faq faq) {
        return faqRepository.save(faq)
                .map(savedFaq -> ResponseEntity.status(HttpStatus.CREATED).body(savedFaq));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFaq(@PathVariable("id") Long id) {
        return faqRepository.findById(id)
                .flatMap(existingFaq ->
                        faqRepository.delete(existingFaq)
                                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
