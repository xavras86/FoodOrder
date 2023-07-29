package pl.xavras.FoodOrder.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"name", "surname", "phone", "email"})
public class Customer {

    Integer customerId;
    String name;
    String surname;
    String phone;
    String email;

}
