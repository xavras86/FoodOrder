package pl.xavras.FoodOrder.api.dto;

import lombok.*;
import pl.xavras.FoodOrder.domain.MenuItemOrder;

import java.math.BigDecimal;
import java.util.Set;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    String orderNumber;
    String receivedDateTime;
    String completedDateTime;
    Boolean cancelled;
    Boolean completed;
    BigDecimal totalValue;
    CustomerDTO customer;
    AddressDTO address;
    RestaurantDTO restaurant;
    Set<MenuItemOrder> menuItemOrders;
}
