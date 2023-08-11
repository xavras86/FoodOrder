package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.OwnerService;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.exception.NotFoundException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class OwnerOrderController {


    public static final String ORDERS_OWNER = "/orders-owner";
    public static final String ORDERS_COMPLETE = "/orders/complete/{orderNumber}";
    private final OrderService orderService;
    private final OwnerService ownerService;


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

    @PutMapping(ORDERS_COMPLETE)
    public String completeOrder(@PathVariable String orderNumber) {
        Order orderToComplete = orderService.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new NotFoundException("Could not find order with orderNumber: " + orderNumber));

        if (!orderToComplete.getCompleted() && Objects.isNull(orderToComplete.getCompletedDateTime())) {
            orderService.completeOrder(orderToComplete);
        }

        return "redirect:/orders-owner";
    }
}

