package pl.xavras.FoodOrder.domain;

import lombok.*;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"name", "phone", "email"})
public class Restaurant {

    String name;
    String phone;
    String email;
    Address address;
    Owner owner;
    Set<RestaurantStreet> restaurantStreets;
    Set<MenuItem> menuItems;


}
