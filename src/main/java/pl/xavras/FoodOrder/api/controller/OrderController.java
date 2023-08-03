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
import pl.xavras.FoodOrder.business.CustomerService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.domain.Order;

import java.util.Set;

@Controller
@AllArgsConstructor
@Slf4j
public class OrderController {

    public static final String ORDERS = "/orders";
    public static final String PLACE_ORDER = "/order/add";
    private final OrderService orderService;
    private  final OrderMapper orderMapper;

    private final CustomerService customerService;


    @GetMapping(ORDERS)
    public String orders(Model model) {

        String activeCustomerEmail = customerService.activeCustomer().getEmail();

//        var allByCustomerOrders = orderService.findByCustomerEmail(activeCustomerEmail)
//                .stream().map(orderMapper::mapToDTO).toList();

        Set<Order> byCustomerEmail = orderService.findByCustomerEmail(activeCustomerEmail);

        

        //todo zmienic tak zeby bylo zalezne od DTO a nie obiektu domenowego


//        model.addAttribute("orders", allByCustomerOrders);
        model.addAttribute("orders", byCustomerEmail);
        model.addAttribute("orderDataDTO", new CustomerAddressOrderDTO());

        return "customerOrders";
    }



}

