package com.pawilion;

import com.pawilion.model.Customer;
import com.pawilion.model.Pet;

public class TestData {

  public static Pet buildPet(Customer customer, Integer age, String name, String species) {
    return Pet.builder().species(species).customerId(customer.getId()).name(name).age(age).build();
  }

  public static Customer buildCustomer(String name, String email, String phone) {
    return Customer.builder().email(email).name(name).phone(phone).build();
  }

  public static Customer buildTestCustomer() {
    return Customer.builder()
        .email("testemail@email.com")
        .name("Test")
        .phone("+48572572572")
        .build();
  }
}
