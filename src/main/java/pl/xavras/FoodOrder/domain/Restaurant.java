package pl.xavras.FoodOrder.domain;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
@ToString(of = {"restaurantId","name", "phone", "email"})
public class Restaurant {

    Integer restaurantId;
    String name;
    String phone;
    String email;
    Address address;
    Owner owner;

}
