package pl.xavras.FoodOrder.domain;

import lombok.*;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "streetId")
@ToString(of = {"city", "streetName" })
public class Street {

    Integer streetId;
    String city;
    String streetName;
    Set<RestaurantStreet> restaurantStreets;

}
