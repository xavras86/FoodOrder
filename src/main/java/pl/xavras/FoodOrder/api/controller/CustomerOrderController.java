package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import pl.xavras.FoodOrder.business.CustomerService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.exception.NotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class CustomerOrderController {

    public static final String ORDERS = "/orders";
    public static final String ORDERS_CANCEL = "/orders/cancel/{orderNumber}";
    private final OrderService orderService;
    private final CustomerService customerService;

    @GetMapping(ORDERS)
    public String orders(Model model) {
        String activeCustomerEmail = customerService.activeCustomer().getEmail();
        Set<Order> allCustomerOrders = orderService.findByCustomerEmail(activeCustomerEmail);

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

        return "customer-orders";
    }

    @PutMapping(ORDERS_CANCEL)
    public String cancelOrder(@PathVariable String orderNumber) {
        Order orderToCancel = orderService.findByOrderNumber(orderNumber);

            orderService.cancelOrder(orderToCancel);

        return "redirect:/orders";
    }
}

