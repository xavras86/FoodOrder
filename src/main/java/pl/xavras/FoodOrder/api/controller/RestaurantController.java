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

import java.util.List;


@Controller
@AllArgsConstructor
@Slf4j
public class RestaurantController {

    public static final String RESTAURANT = "/restaurants";


    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;



    @GetMapping(RESTAURANT)
    public String restaurants(Model model) {
        List<RestaurantDTO> allRestaurants = restaurantService.findAll().stream()
                .map(restaurantMapper::map).toList();
        model.addAttribute("restaurants", allRestaurants);
        return "restaurants";
    }

}



