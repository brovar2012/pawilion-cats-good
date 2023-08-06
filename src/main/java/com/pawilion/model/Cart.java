package com.pawilion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("carts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cart {
  @Id private Long id;

  private Long customerId;

  private Long productId;

  private int quantity;
}
