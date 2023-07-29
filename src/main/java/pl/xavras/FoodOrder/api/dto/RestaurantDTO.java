package pl.xavras.FoodOrder.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {


    private String name;
    private String phone;
    private String email;

}
