package pl.xavras.FoodOrder.domain;

import lombok.*;


@With
@Value
@Builder
@Data
public class MenuItemOrder {

    Integer menuItemOrderId;
    Integer quantity;
    MenuItem menuItem;
    Order order;
}

