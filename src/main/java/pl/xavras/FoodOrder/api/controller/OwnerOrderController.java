package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import pl.xavras.FoodOrder.business.AddressService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.OwnerService;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.exception.NotFoundException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class OwnerOrderController {


    public static final String ORDERS_OWNER = "/owner/orders";
    public static final String ORDERS_COMPLETE = "/orders/complete/{orderNumber}";
    public static final String ORDERS_DETAILS = "/owner/orders/{orderNumber}";
    private final OrderService orderService;
    private final OwnerService ownerService;
    private final AddressService addressService;


    @GetMapping(ORDERS_OWNER)
    public String orders(Model model) {
        String activeOwnerEmail = ownerService.activeOwner().getEmail();
        Set<Order> allCustomerOrders = orderService.findByOwnerEmail(activeOwnerEmail);

        Set<Order> cancelledOrders = allCustomerOrders.stream()
                .filter(a -> a.getCancelled())
                .collect(Collectors.toSet());

        Set<Order> completedOrders = allCustomerOrders.stream()
                .filter(a -> a.getCompleted())
                .collect(Collectors.toSet());

        Set<Order> activeOrders = allCustomerOrders.stream()
                .filter(a -> (!a.getCancelled() && !a.getCompleted()))
                .collect(Collectors.toSet());


        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("cancelledOrders", cancelledOrders);
        model.addAttribute("activeOrders", activeOrders);
        model.addAttribute("orderService", orderService);

        return "owner-orders";
    }

    @GetMapping(ORDERS_DETAILS)
    public String orderPlaced(@PathVariable String orderNumber,
                              Model model) {

        Order order = orderService.findByOrderNumber(orderNumber);
        Address deliveryAddress = order.getAddress();
        Address restaurantAddress = order.getRestaurant().getAddress();
        Set<MenuItemOrder> menuItemOrders = order.getMenuItemOrders();
        String status = orderService.orderStatus(order);
        String mapUrl = addressService.createMapUrl(restaurantAddress, deliveryAddress);

        model.addAttribute("order", order);
        model.addAttribute("deliveryAddress", deliveryAddress);
        model.addAttribute("restaurantAddress", restaurantAddress);
        model.addAttribute("menuItemOrders", menuItemOrders);
        model.addAttribute("status", status);
        model.addAttribute("mapUrl",  mapUrl);

        return "owner-order-details";
    }

    @PutMapping(ORDERS_COMPLETE)
    public String completeOrder(@PathVariable String orderNumber) {
        Order orderToComplete = orderService.findByOrderNumber(orderNumber);
        if (!orderToComplete.getCompleted() && Objects.isNull(orderToComplete.getCompletedDateTime())) {
            orderService.completeOrder(orderToComplete);
        }

        return "redirect:/owner/orders";
    }
}

