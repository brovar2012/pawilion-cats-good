package com.pawilion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("pets")
public class Pet {

  @Id private Long id;

  private String name;

  private String species;

  private Integer age;

  public Pet(String name, String species, Integer age) {
    this.name = name;
    this.species = species;
    this.age = age;
  }
}
