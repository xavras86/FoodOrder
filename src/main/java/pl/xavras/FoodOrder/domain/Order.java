package pl.xavras.FoodOrder.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@With
@Value
@Builder
@Data
public class Order {

    String orderNumber;
    OffsetDateTime receivedDateTime;
    OffsetDateTime completedDateTime;
    Boolean cancelled;
    Boolean completed;
    BigDecimal totalValue;
    Customer customer;
    Address address;
    Restaurant restaurant;
    Set<MenuItemOrder> menuItemOrders;
}
