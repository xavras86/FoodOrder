package pl.xavras.FoodOrder.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xavras.FoodOrder.api.dto.OrdersDTO;
import pl.xavras.FoodOrder.api.dto.OrdersValueDTO;
import pl.xavras.FoodOrder.api.dto.mapper.OrderMapper;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.domain.Order;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping
@AllArgsConstructor
public class OrderRestController {
    public static final String ORDERS = "/api/orders";
    public static final String ORDERS_RESTAURANT_NAME_COMPLETED = "/api/orders/completed/{restaurantName}";
    public static final String ORDERS_RESTAURANT_NAME_ACTIVE = "/api/orders/active/{restaurantName}";
    public static final String ORDERS_RESTAURANT_NAME_CANCELED = "/api/orders/cancelled/{restaurantName}";
    public static final String ORDERS_RESTAURANT_COMPLETED_ORDERS_VALUES_OVER_PERIOD = "/api/orders/completed/numbersValuesMap/{restaurantName}/{periodDays}";
    public static final String ORDERS_RESTAURANT_CANCELED_DELETE = "/api/orders/cancelled/delete/{orderNumber}";
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Operation(summary = "Retrieving a list of all orders from the system.")
    @GetMapping(ORDERS)
    public OrdersDTO ordersList() {
        return OrdersDTO.of(orderService.findAll()
                .stream().map(orderMapper::mapToDTO).toList());

    }
    @Operation(summary = "Retrieving a list of all completed orders from the system, for a restaurant based on the name.")
    @GetMapping(ORDERS_RESTAURANT_NAME_COMPLETED)
    public OrdersDTO completedOrdersListByRestaurant(@PathVariable String restaurantName) {

        return OrdersDTO.of(orderService.findByRestaurantName(restaurantName).stream()
                .filter(Order::getCompleted)
                .map(orderMapper::mapToDTO).toList());
    }
    @Operation(summary = "Retrieving a list of all canceled orders from the system, for a restaurant based on the name.")
    @GetMapping(ORDERS_RESTAURANT_NAME_CANCELED)
    public OrdersDTO cancelledOrdersListByRestaurant(@PathVariable String restaurantName) {

        return OrdersDTO.of(orderService.findByRestaurantName(restaurantName).stream()
                .filter(Order::getCancelled)
                .map(orderMapper::mapToDTO).toList());
    }
    @Operation(summary = "Retrieving a list of all active orders from the system, for a restaurant based on the name.")
    @GetMapping(ORDERS_RESTAURANT_NAME_ACTIVE)
    public OrdersDTO activeOrdersListByRestaurant(@PathVariable String restaurantName) {

        return OrdersDTO.of(orderService.findByRestaurantName(restaurantName).stream()
                .filter(a -> ("Waiting".equals(orderService.orderStatus(a))))
                .map(orderMapper::mapToDTO).toList());
    }
    @Operation(summary = "Fetching a map of fulfilled order numbers and their values for a specific restaurant based on the name within a specified time range (number of days ago).")
    @GetMapping(ORDERS_RESTAURANT_COMPLETED_ORDERS_VALUES_OVER_PERIOD)
    public OrdersValueDTO completedOrderAndValues(@PathVariable String restaurantName, Integer periodDays) {
        OffsetDateTime startMoment = OffsetDateTime.now().minus(periodDays, ChronoUnit.DAYS);

        return OrdersValueDTO.of(orderService.findByRestaurantName(restaurantName).stream()
                .filter(Order::getCompleted)
                .filter(order -> order.getCompletedDateTime().isAfter(startMoment))
                .collect(Collectors.toMap(Order::getOrderNumber, Order::getTotalValue)));
    }
    @Operation(summary = "Deleting a canceled order based on the order number.")
    @DeleteMapping(ORDERS_RESTAURANT_CANCELED_DELETE)
    public ResponseEntity<?> deleteCanceledOrder(@PathVariable String orderNumber) {

        var byOrderNumber = orderService.findOptByOrderNumber(orderNumber);
        if (byOrderNumber.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!byOrderNumber.get().getCancelled()) {
            return ResponseEntity.badRequest().build();
        }
        orderService.deleteByOrderNumber(orderNumber);
        return ResponseEntity.noContent().build();
    }
}




