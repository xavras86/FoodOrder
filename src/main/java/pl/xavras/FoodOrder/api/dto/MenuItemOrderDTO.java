package pl.xavras.FoodOrder.api.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of ={ "quantity", "menuItem"})
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemOrderDTO {


    private int quantity;
    private MenuItemDTO menuItem;



    //   private RestaurantDTO restaurant;

}
