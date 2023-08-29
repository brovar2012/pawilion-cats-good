package com.pawilion.repository;

import com.pawilion.model.Faq;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FaqRepository extends ReactiveCrudRepository<Faq, Long> {
}
