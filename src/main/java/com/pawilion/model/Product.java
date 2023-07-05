package com.pawilion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  @Id private Long id;

  private String name;

  private String description;

  private double price;

  public Product(String name, String description, double price) {
    this.name = name;
    this.description = description;
    this.price = price;
  }
}
