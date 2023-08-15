package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@AllArgsConstructor
@Slf4j
public class CustomerOrdersController {

    public static final String ORDERS = "/customer/orders";
    public static final String ORDERS_CANCEL = "/customer/orders/cancel/{orderNumber}";
    public static final String CUSTOMER_ORDERS_DETAILS = "/customer/orders/{orderNumber}";
    public static final String CUSTOMER_ORDERS_DETAILS_FORM = "/customer/orders-form";
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
        Pageable activePageable = utilityService.createPagable(activePageSize, activePageNumber, activeSortBy, activeSortDirection);
        Pageable cancelledPageable = utilityService.createPagable(cancelledPageSize, cancelledPageNumber, cancelledSortBy, cancelledSortDirection);
        Pageable completedPageable = utilityService.createPagable(completedPageSize, completedPageNumber, completedSortBy, completedSortDirection);

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


    @GetMapping(CUSTOMER_ORDERS_DETAILS)
    public String orderDetails(@PathVariable String orderNumber,
                               Model model) {

        Order order = orderService.findByOrderNumber(orderNumber);

        if (!order.getCustomer().equals(customerService.activeCustomer())) {
            throw new IllegalArgumentException(
                    String.format("Unfortunately, you cannot view details of order [%s], as it does not belong to you.",
                            orderNumber));
        }
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

    @GetMapping(CUSTOMER_ORDERS_DETAILS_FORM)
    public String orderDetailsForm(@RequestParam String orderNumber, Model model) {

        return orderDetails(orderNumber, model);
    }


    @PutMapping(ORDERS_CANCEL)
    public String cancelOrder(@PathVariable String orderNumber) {
        Order orderToCancel = orderService.findByOrderNumber(orderNumber);

        orderService.cancelOrder(orderToCancel);

        return "redirect:/customer/orders";
    }


}

