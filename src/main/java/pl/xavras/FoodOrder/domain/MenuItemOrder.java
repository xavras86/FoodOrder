package pl.xavras.FoodOrder.domain;

import lombok.*;


@With
@Value
@Builder
@EqualsAndHashCode(of = "menuItemOrdersId")
@ToString(of = {"menuItemOrdersId", "quantity"})
public class MenuItemOrder {

    Integer menuItemOrdersId;
    Integer quantity;
    MenuItem menuItem;
    Order order;
}

