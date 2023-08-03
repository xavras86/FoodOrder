package pl.xavras.FoodOrder.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode
@ToString
public class Address {

    String country;
    String city;
    String street;
    String buildingNumber;
}
