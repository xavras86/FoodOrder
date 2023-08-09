package pl.xavras.FoodOrder.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@With
@Value
@Builder
@Data
@ToString(of = { "menuItemId","name", "price", "category", "description"})
public class MenuItem {
    Integer menuItemId;
    String name;
    BigDecimal price;
    String category;
    String description;
    Boolean available;
    Restaurant restaurant;
    Set<MenuItemOrder> menuItemOrders;


}
