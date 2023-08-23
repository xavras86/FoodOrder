package pl.xavras.FoodOrder.domain;

import lombok.*;
import pl.xavras.FoodOrder.infrastructure.database.entity.MealCategory;

import java.math.BigDecimal;

@With
@Value
@Builder
@Data
@Setter
@ToString(of = { "menuItemId","name", "price", "category", "description"})
public class MenuItem {
    Integer menuItemId;
    String name;
    BigDecimal price;
    MealCategory category;
    String description;
    Boolean available;
    Restaurant restaurant;
    byte[] image;

}
