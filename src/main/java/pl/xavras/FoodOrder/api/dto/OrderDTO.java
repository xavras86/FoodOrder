package pl.xavras.FoodOrder.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.xavras.FoodOrder.domain.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

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
