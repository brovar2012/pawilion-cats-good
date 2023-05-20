package com.pawilion.controller;

import com.pawilion.model.Order;
import com.pawilion.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
  private final OrderRepository orderRepository;

  @PostMapping
  public Mono<Order> createOrder(@RequestBody Order order) {
    return orderRepository.save(order);
  }

  @GetMapping
  public Flux<Order> getAllOrders() {
    return orderRepository.findAll();
  }
}
