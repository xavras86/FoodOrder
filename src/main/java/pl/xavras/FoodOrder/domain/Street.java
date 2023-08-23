package pl.xavras.FoodOrder.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = {"city","streetName"})
@ToString(of = {"city", "streetName" })
public class Street {

    Integer streetId;
    String city;
    String streetName;

}
