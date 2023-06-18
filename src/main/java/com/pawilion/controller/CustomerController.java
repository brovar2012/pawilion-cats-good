package com.pawilion.controller;

import com.pawilion.model.Customer;
import com.pawilion.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerRepository customerRepository;

  @PostMapping
  public Mono<Customer> createCustomer(@RequestBody Customer customer) {
    return customerRepository.save(customer);
  }

  @GetMapping
  public Flux<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }
}
