package pl.xavras.FoodOrder.api.dto;

import lombok.*;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RestaurantsDTO {

   private List<RestaurantDTO> restaurants;
}
