package pl.xavras.FoodOrder.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;


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

