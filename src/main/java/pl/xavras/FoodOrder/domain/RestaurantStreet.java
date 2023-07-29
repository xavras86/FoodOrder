package pl.xavras.FoodOrder.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "restaurantStreetId")
@ToString(of = {"restaurantStreetId"})
public class RestaurantStreet {

    Integer restaurantStreetId;
    Street street;
    Restaurant restaurant;


}
