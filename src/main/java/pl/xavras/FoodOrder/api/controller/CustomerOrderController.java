package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.xavras.FoodOrder.api.dto.MenuItemDTO;
import pl.xavras.FoodOrder.business.AddressService;
import pl.xavras.FoodOrder.business.CustomerService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class CustomerOrderController {

    public static final String ORDERS = "/customer/orders";
    public static final String ORDERS_CANCEL = "/customer/orders/cancel/{orderNumber}";
    public static final String ORDERS_DETAILS = "/customer/orders/{orderNumber}";
    private final OrderService orderService;
    private final CustomerService customerService;
    private final AddressService addressService;
    private final UtilityService utilityService;

    @GetMapping(ORDERS)
    public String orders(Model model,
                         @RequestParam(defaultValue = "5") int activePageSize,
                         @RequestParam(defaultValue = "5") int cancelledPageSize,
                         @RequestParam(defaultValue = "5") int completedPageSize,
                         @RequestParam(defaultValue = "1") int activePageNumber,
                         @RequestParam(defaultValue = "1") int cancelledPageNumber,
                         @RequestParam(defaultValue = "1") int completedPageNumber,
                         @RequestParam(defaultValue = "receivedDateTime") String activeSortBy,
                         @RequestParam(defaultValue = "receivedDateTime") String cancelledSortBy,
                         @RequestParam(defaultValue = "receivedDateTime") String completedSortBy,
                         @RequestParam(defaultValue = "desc") String activeSortDirection,
                         @RequestParam(defaultValue = "desc") String cancelledSortDirection,
                         @RequestParam(defaultValue = "desc") String completedSortDirection
    ) {

        Pageable activePageable = PageRequest.of(
                activePageNumber - 1,
                activePageSize,
                Sort.by(Sort.Direction.fromString(activeSortDirection), activeSortBy));

        Pageable cancelledPageable = PageRequest.of(
                cancelledPageNumber - 1,
                cancelledPageSize,
                Sort.by(Sort.Direction.fromString(cancelledSortDirection), cancelledSortBy));

        Pageable completedPageable = PageRequest.of(
                completedPageNumber - 1,
                completedPageSize,
                Sort.by(Sort.Direction.fromString(completedSortDirection), completedSortBy));


        Page<Order> activeOrders = orderService.findByCustomerAndCancelledAndCompletedPaged(activePageable, customerService.activeCustomer(), false, false);
        List<Integer> activePageNumbers = utilityService.generatePageNumbers(activePageNumber, activeOrders.getTotalPages());
        Map<Order, Boolean> activeOrdersMap = getCollect(activeOrders);

        Page<Order> cancelledOrders = orderService.findByCustomerAndCancelledAndCompletedPaged(cancelledPageable, customerService.activeCustomer(), true, false);
        List<Integer> cancelledPageNumbers = utilityService.generatePageNumbers(cancelledPageNumber, cancelledOrders.getTotalPages());

        Page<Order> completedOrders = orderService.findByCustomerAndCancelledAndCompletedPaged(completedPageable, customerService.activeCustomer(), false, true);
        List<Integer> completedPageNumbers = utilityService.generatePageNumbers(completedPageNumber, completedOrders.getTotalPages());

        model.addAttribute("activeOrdersMap", activeOrdersMap);
        model.addAttribute("activeCurrentPage", activePageNumber);
        model.addAttribute("activeTotalPages", activeOrders.getTotalPages());
        model.addAttribute("activePageSize", activePageSize);
        model.addAttribute("activeSortBy", activeSortBy);
        model.addAttribute("activeSortDirection", activeSortDirection);
        model.addAttribute("activePageNumbers", activePageNumbers);

        model.addAttribute("cancelledOrders", cancelledOrders.getContent());
        model.addAttribute("cancelledCurrentPage", cancelledPageNumber);
        model.addAttribute("cancelledTotalPages", cancelledOrders.getTotalPages());
        model.addAttribute("cancelledPageSize", cancelledPageSize);
        model.addAttribute("cancelledSortBy", cancelledSortBy);
        model.addAttribute("cancelledSortDirection", cancelledSortDirection);
        model.addAttribute("cancelledPageNumbers", completedPageNumbers);

        model.addAttribute("completedOrders", completedOrders.getContent());
        model.addAttribute("completedCurrentPage", completedPageNumber);
        model.addAttribute("completedTotalPages", completedOrders.getTotalPages());
        model.addAttribute("completedPageSize", completedPageSize);
        model.addAttribute("completedSortBy", completedSortBy);
        model.addAttribute("completedSortDirection", completedSortDirection);
        model.addAttribute("completedPageNumbers", cancelledPageNumbers);

        return "customer-orders";
    }

    //creating map with order key and key with flag telling if you still can cancel the order
    private LinkedHashMap<Order, Boolean> getCollect(Page<Order> activeOrders) {
        return activeOrders.stream()
                .collect(Collectors.toMap(
                        order -> order,
                        orderService::isCancellable,
                        (existingValue, newValue) -> existingValue,
                        LinkedHashMap::new
                ));
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

        return "customer-order-details";
    }

    @PutMapping(ORDERS_CANCEL)
    public String cancelOrder(@PathVariable String orderNumber) {
        Order orderToCancel = orderService.findByOrderNumber(orderNumber);

        orderService.cancelOrder(orderToCancel);

        return "redirect:/customer/orders";
    }


}

