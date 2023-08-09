package pl.xavras.FoodOrder.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.xavras.FoodOrder.infrastructure.database.entity.MealCategory;

import java.math.BigDecimal;

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
