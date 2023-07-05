package com.pawilion.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("orders")
@Data
public class Order {
  @Id private Long id;
  private Long customerId;
  private BigDecimal totalAmount;
  private LocalDateTime orderDate;
}
