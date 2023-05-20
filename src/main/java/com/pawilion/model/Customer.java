package com.pawilion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("customers")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  @Id private Long id;

  private String firstName;

  private String lastName;

  private String email;

  private String phone;
}
