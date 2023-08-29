package com.pawilion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("reviews")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Review {
  @Id private Long id;

  private Long productId;

  private String comment;

  private int rating;
}
