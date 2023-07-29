package pl.xavras.FoodOrder.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
@Data
public class Address {

    String country;
    String city;
    String street;
    String buildingNumber;
}
