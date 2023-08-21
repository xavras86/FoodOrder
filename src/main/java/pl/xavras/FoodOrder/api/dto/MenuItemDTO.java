package pl.xavras.FoodOrder.api.dto;

import lombok.*;
import pl.xavras.FoodOrder.infrastructure.database.entity.MealCategory;

import java.math.BigDecimal;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {

   private Integer menuItemId;
   private String name;
   private BigDecimal price;
   private MealCategory category;
   private String description;
   private Boolean available;
   private byte[] image;

}
