package pl.xavras.FoodOrder.domain;

import lombok.*;

import java.math.BigDecimal;

@With
@Value
@Builder
@Data
@ToString(of = {"name", "price", "category", "description"})
public class MenuItem {
    Integer menuItemId;
    String name;
    BigDecimal price;
    String category;
    String description;
    Restaurant restaurant;
//    Set<MenuItemOrder> menuItemOrders;
    //todo dodac set z menuitemorderami

}
