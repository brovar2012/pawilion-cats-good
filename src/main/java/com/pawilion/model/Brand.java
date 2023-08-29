package com.pawilion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("brands")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Brand {
  @Id private Long id;

  private String name;

  private String description;
}
