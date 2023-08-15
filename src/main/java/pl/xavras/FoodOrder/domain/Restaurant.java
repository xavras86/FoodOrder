package pl.xavras.FoodOrder.domain;

import lombok.*;

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
    Set<RestaurantStreet> restaurantStreets;
    Set<MenuItem> menuItems;



}
