package pl.xavras.FoodOrder.domain;

import lombok.*;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "streetId")
@ToString(of = {"city", "street" })
public class Street {

    Integer streetId;
    String city;
    String street;
    Set<RestaurantStreet> restaurantStreets;

}
