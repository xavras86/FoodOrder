package pl.xavras.FoodOrder.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xavras.FoodOrder.api.dto.OrderDTO;
import pl.xavras.FoodOrder.api.dto.OrdersDTO;
import pl.xavras.FoodOrder.api.dto.OrdersValueDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
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
    public static final String ORDER = "/api/order/{orderNumber}";
    public static final String ORDERS_RESTAURANT_NAME_COMPLETED = "/api/orders/completed/{restaurantName}";
    public static final String ORDERS_RESTAURANT_NAME_ACTIVE = "/api/orders/active/{restaurantName}";
    public static final String ORDERS_RESTAURANT_NAME_CANCELED = "/api/orders/cancelled/{restaurantName}";
    public static final String ORDERS_RESTAURANT_COMPLETED_ORDERS_VALUES_OVER_PERIOD = "/api/orders/completed/numbersValuesMap/{restaurantName}/{periodDays}";
    public static final String ORDERS_RESTAURANT_CANCELED_DELETE = "/api/orders/cancelled/delete/{orderNumber}";
    private final OrderService orderService;
    private final OrderMapper orderMapper;


    @Operation(summary = "Fetching details about an order based on its number.")
    @GetMapping(value = ORDER, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDTO orderDetails(
            @Parameter(description = "Number of on order.")
            @PathVariable String orderNumber) {
        return orderMapper.mapToDTO(orderService.findByOrderNumber(orderNumber));
    }


    @Operation(summary = "Retrieving a list of all orders from the system.")
    @GetMapping(value = ORDERS, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrdersDTO ordersList() {
        return OrdersDTO.of(orderService.findAll()
                .stream().map(orderMapper::mapToDTO).toList());

    }

    @Operation(summary = "Retrieving a list of all completed orders from the system, for a restaurant based on the name.")
    @GetMapping(value = ORDERS_RESTAURANT_NAME_COMPLETED, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrdersDTO completedOrdersListByRestaurant(
            @Parameter(description = "Name of the restaurant.")
            @PathVariable String restaurantName) {

        return OrdersDTO.of(orderService.findByRestaurantName(restaurantName).stream()
                .filter(Order::getCompleted)
                .map(orderMapper::mapToDTO).toList());
    }

    @Operation(summary = "Retrieving a list of all canceled orders from the system, for a restaurant based on the name.")
    @GetMapping(value = ORDERS_RESTAURANT_NAME_CANCELED, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrdersDTO cancelledOrdersListByRestaurant(
            @Parameter(description = "Name of the restaurant.")
            @PathVariable String restaurantName) {

        return OrdersDTO.of(orderService.findByRestaurantName(restaurantName).stream()
                .filter(Order::getCancelled)
                .map(orderMapper::mapToDTO).toList());
    }

    @Operation(summary = "Retrieving a list of all active orders from the system, for a restaurant based on the name.")
    @GetMapping(value = ORDERS_RESTAURANT_NAME_ACTIVE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrdersDTO activeOrdersListByRestaurant(
            @Parameter(description = "Name of the restaurant.")
            @PathVariable String restaurantName) {

        return OrdersDTO.of(orderService.findByRestaurantName(restaurantName).stream()
                .filter(a -> ("Waiting".equals(orderService.orderStatus(a))))
                .map(orderMapper::mapToDTO).toList());
    }

    @Operation(summary = "Fetching a map of fulfilled order numbers and their values for a specific restaurant based on the name within a specified time range (number of days ago).")
    @GetMapping(value = ORDERS_RESTAURANT_COMPLETED_ORDERS_VALUES_OVER_PERIOD, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrdersValueDTO completedOrderAndValues(
            @Parameter(description = "Name of the restaurant.")
            @PathVariable String restaurantName,
            @Parameter(description = "Number of days back from which values will be counted.")
            @PathVariable Integer periodDays) {
        OffsetDateTime startMoment = OffsetDateTime.now().minus(periodDays, ChronoUnit.DAYS);

        return OrdersValueDTO.of(orderService.findByRestaurantName(restaurantName).stream()
                .filter(Order::getCompleted)
                .filter(order -> order.getCompletedDateTime().isAfter(startMoment))
                .collect(Collectors.toMap(Order::getOrderNumber, Order::getTotalValue)));
    }

    @Operation(summary = "Deleting a canceled order based on the order number.")
    @ApiResponse(responseCode = "404",
            description = "The order with provided number was not found.")
    @ApiResponse(responseCode = "400",
            description = "The order with provided number is not canceled.")
    @ApiResponse(responseCode = "204",
            description = "The order has been deleted.")
    @DeleteMapping(value = ORDERS_RESTAURANT_CANCELED_DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCanceledOrder(
            @Parameter(description = "Number of the canceled order")
            @PathVariable String orderNumber) {

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




