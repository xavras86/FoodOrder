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
import pl.xavras.FoodOrder.api.dto.AddressDTO;
import pl.xavras.FoodOrder.api.dto.OrderDTO;
import pl.xavras.FoodOrder.api.dto.mapper.AddressMapper;
import pl.xavras.FoodOrder.api.dto.mapper.OrderMapper;
import pl.xavras.FoodOrder.business.AddressService;
import pl.xavras.FoodOrder.business.CustomerService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class CustomerOrdersController {

    public static final String ORDERS = "/customer/orders";
    public static final String ORDERS_CANCEL = "/customer/orders/cancel/{orderNumber}";
    public static final String ORDERS_DETAILS = "/customer/orders/{orderNumber}";
    private final OrderService orderService;
    private final CustomerService customerService;
    private final AddressService addressService;
    private final UtilityService utilityService;

    private final AddressMapper addressMapper;
    private final OrderMapper orderMapper;

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


        Customer activeCustomer = customerService.activeCustomer();

        Page<Order> activeOrders = orderService
                .findByCustomerAndCancelledAndCompletedPaged(activePageable, activeCustomer, false, false);
        List<Integer> activePageNumbers = utilityService.generatePageNumbers(activePageNumber, activeOrders.getTotalPages());

        Map<OrderDTO, Boolean> activeOrdersMap = orderService.createOrderCancellationMap(activeOrders);

        Page<OrderDTO> cancelledOrders = orderService
                .findByCustomerAndCancelledAndCompletedPaged(cancelledPageable, activeCustomer, true, false)
                .map(orderMapper::mapToDTO);

        List<Integer> cancelledPageNumbers = utilityService.generatePageNumbers(cancelledPageNumber, cancelledOrders.getTotalPages());

        Page<OrderDTO> completedOrders = orderService
                .findByCustomerAndCancelledAndCompletedPaged(completedPageable, activeCustomer, false, true)
                .map(orderMapper::mapToDTO);

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
        model.addAttribute("cancelledPageNumbers", cancelledPageNumbers);

        model.addAttribute("completedOrders", completedOrders.getContent());
        model.addAttribute("completedCurrentPage", completedPageNumber);
        model.addAttribute("completedTotalPages", completedOrders.getTotalPages());
        model.addAttribute("completedPageSize", completedPageSize);
        model.addAttribute("completedSortBy", completedSortBy);
        model.addAttribute("completedSortDirection", completedSortDirection);
        model.addAttribute("completedPageNumbers", completedPageNumbers);

        return "customer-orders";
    }



    @GetMapping(ORDERS_DETAILS)
    public String orderPlaced(@PathVariable String orderNumber,
                              Model model) {

        Order order = orderService.findByOrderNumber(orderNumber);
        OrderDTO orderDTO = orderMapper.mapToDTO(order);
        AddressDTO deliveryAddressDTO = addressMapper.map(order.getAddress());
        AddressDTO restaurantAddressDTO = addressMapper.map(order.getRestaurant().getAddress());
        Set<MenuItemOrder> menuItemOrders = order.getMenuItemOrders();
        String status = orderService.orderStatus(order);
        String mapUrl = addressService.createMapUrl(restaurantAddressDTO, deliveryAddressDTO);

        model.addAttribute("order", orderDTO);
        model.addAttribute("deliveryAddress", deliveryAddressDTO);
        model.addAttribute("restaurantAddress", restaurantAddressDTO);
        model.addAttribute("menuItemOrders", menuItemOrders);
        model.addAttribute("status", status);
        model.addAttribute("mapUrl", mapUrl);

        return "customer-order-details";
    }

    @PutMapping(ORDERS_CANCEL)
    public String cancelOrder(@PathVariable String orderNumber) {
        Order orderToCancel = orderService.findByOrderNumber(orderNumber);

        orderService.cancelOrder(orderToCancel);

        return "redirect:/customer/orders";
    }


}

