package com.pawilion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("pets")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pet {

  @Id private Long id;

  private String name;

  private String species;

  private Integer age;

  @Column("customer_id")
  private Long customerId;
}
