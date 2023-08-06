package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.mapper.*;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.OwnerService;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.List;
import java.util.Set;


@Controller
@AllArgsConstructor
@Slf4j
public class RestaurantController {

    public static final String RESTAURANT = "/restaurants";
    public static final String RESTAURANT_BY_OWNER = "/restaurants/owner";
    private static final String RESTAURANT_BY_NAME = "/restaurants/{restaurantName}";
    private static final String RESTAURANT_DELIVERY = "/restaurants/deliveryAddress";
    private static final String RESTAURANT_ADDITEMS = "/restaurants/addItems";
    ;

    private final RestaurantService restaurantService;
    private final OwnerService ownerService;
    private final OrderService orderService;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;
    private final AddressMapper addressMapper;
    private final OwnerMapper ownerMapper;
    private final OrderMapper orderMapper;

    private final MenuItemOrderMapper menuItemOrderMapper;


    @GetMapping(RESTAURANT)
    public String restaurants(Model model) {
        List<RestaurantDTO> allRestaurants = restaurantService.findAll().stream()
                .map(restaurantMapper::map).toList();
        model.addAttribute("restaurants", allRestaurants);
        return "restaurants";
    }

    @GetMapping(value = RESTAURANT_BY_OWNER)
    public String showRestaurantsByOwner(Model model) {

        String email = ownerService.activeOwner().getEmail();
        Set<Restaurant> restaurantByOwner = restaurantService.findByOwnerEmail(email);
        model.addAttribute("ownerEmail", email);
        model.addAttribute("restaurantByOwner", restaurantByOwner);
        return "restaurants-by-owner";
    }
}



