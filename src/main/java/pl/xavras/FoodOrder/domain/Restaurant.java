package pl.xavras.FoodOrder.domain;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
