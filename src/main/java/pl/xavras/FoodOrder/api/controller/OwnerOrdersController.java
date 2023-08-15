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
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.OwnerService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.Owner;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
@AllArgsConstructor
@Slf4j
public class OwnerOrdersController {


    public static final String ORDERS_OWNER = "/owner/orders";
    public static final String ORDERS_COMPLETE = "/orders/complete/{orderNumber}";
    public static final String ORDERS_DETAILS = "/owner/orders/{orderNumber}";
    private final OrderService orderService;
    private final OwnerService ownerService;
    private final AddressService addressService;
    private final UtilityService utilityService;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;

    @GetMapping(ORDERS_OWNER)
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

        Owner activeOwner = ownerService.activeOwner();

        Page<OrderDTO> activeOrdersDTO = orderService
                .findByOwnerAndCancelledAndCompletedPaged(activePageable, activeOwner, false, false)
                .map(orderMapper::mapToDTO);
        List<Integer> activePageNumbers = utilityService.generatePageNumbers(activePageNumber, activeOrdersDTO.getTotalPages());


        Page<OrderDTO> cancelledOrdersDTO = orderService
                .findByOwnerAndCancelledAndCompletedPaged(cancelledPageable, activeOwner, true, false)
                .map(orderMapper::mapToDTO);

        List<Integer> cancelledPageNumbers = utilityService.generatePageNumbers(cancelledPageNumber, cancelledOrdersDTO.getTotalPages());

        Page<OrderDTO> completedOrdersDTO = orderService
                .findByOwnerAndCancelledAndCompletedPaged(completedPageable, activeOwner, false, true)
                .map(orderMapper::mapToDTO);

        List<Integer> completedPageNumbers = utilityService.generatePageNumbers(completedPageNumber, completedOrdersDTO.getTotalPages());

        model.addAttribute("activeOrders", activeOrdersDTO.getContent());
        model.addAttribute("activeCurrentPage", activePageNumber);
        model.addAttribute("activeTotalPages", activeOrdersDTO.getTotalPages());
        model.addAttribute("activePageSize", activePageSize);
        model.addAttribute("activeSortBy", activeSortBy);
        model.addAttribute("activeSortDirection", activeSortDirection);
        model.addAttribute("activePageNumbers", activePageNumbers);

        model.addAttribute("cancelledOrders", cancelledOrdersDTO.getContent());
        model.addAttribute("cancelledCurrentPage", cancelledPageNumber);
        model.addAttribute("cancelledTotalPages", cancelledOrdersDTO.getTotalPages());
        model.addAttribute("cancelledPageSize", cancelledPageSize);
        model.addAttribute("cancelledSortBy", cancelledSortBy);
        model.addAttribute("cancelledSortDirection", cancelledSortDirection);
        model.addAttribute("cancelledPageNumbers", cancelledPageNumbers);

        model.addAttribute("completedOrders", completedOrdersDTO.getContent());
        model.addAttribute("completedCurrentPage", completedPageNumber);
        model.addAttribute("completedTotalPages", completedOrdersDTO.getTotalPages());
        model.addAttribute("completedPageSize", completedPageSize);
        model.addAttribute("completedSortBy", completedSortBy);
        model.addAttribute("completedSortDirection", completedSortDirection);
        model.addAttribute("completedPageNumbers", completedPageNumbers);

        return "owner-orders";
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

        model.addAttribute("order", order);
        model.addAttribute("deliveryAddress", deliveryAddressDTO);
        model.addAttribute("restaurantAddress", restaurantAddressDTO);
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

