package pl.xavras.FoodOrder.api.dto;

import lombok.*;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private String country;
    private String city;
    private String street;
    private String buildingNumber;

}
