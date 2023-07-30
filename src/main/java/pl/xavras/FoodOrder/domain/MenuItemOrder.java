package pl.xavras.FoodOrder.domain;

import lombok.*;


@With
@Value
@Builder
@EqualsAndHashCode(of = "menuItemOrderId")
@ToString(of = {"menuItemOrdersId", "quantity"})
public class MenuItemOrder {

    Integer menuItemOrderId;
    Integer quantity;
    MenuItem menuItem;
    Order order;
}

