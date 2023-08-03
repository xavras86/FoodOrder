package pl.xavras.FoodOrder.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode
@ToString(of = {"name", "surname", "phone", "email"})
public class Owner {

    String name;
    String surname;
    String phone;
    String email;

}
