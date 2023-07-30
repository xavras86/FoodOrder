package pl.xavras.FoodOrder.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.xavras.FoodOrder.api.dto.CustomerAddressOrderDTO;
import pl.xavras.FoodOrder.api.dto.mapper.CustomerMapper;
import pl.xavras.FoodOrder.api.dto.mapper.OrderMapper;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.domain.Order;

@Controller
@AllArgsConstructor
@Slf4j
public class OrderController {

    public static final String ORDERS = "/orders";
    public static final String PLACE_ORDER = "/order/add";
    private final OrderService orderService;
    private  final OrderMapper orderMapper;


    @GetMapping(ORDERS)
    public String orders(Model model) {
        var allOrders = orderService.findAll().stream().map(orderMapper::mapToDTO).toList();

        model.addAttribute("orders", allOrders);
        model.addAttribute("orderDataDTO", new CustomerAddressOrderDTO());

        return "orders";
    }


    @PostMapping(value = PLACE_ORDER)
    public String placeOrder(
            @Valid @ModelAttribute("orderDataDTO") CustomerAddressOrderDTO customerAddressOrderDTO,
            Model model
    ) {
        Order order = orderMapper.mapFromDTO(customerAddressOrderDTO);

        orderService.placeOrder(order);

        return "order_placed";
    }
}

