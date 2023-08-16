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
import org.springframework.web.bind.annotation.RequestParam;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.List;


@Controller
@AllArgsConstructor
@Slf4j
public class CustomerRestaurantsController {

    public static final String RESTAURANTS = "/customer/restaurants";


    private final RestaurantService restaurantService;
    private final UtilityService utilityService;


    @GetMapping(RESTAURANTS)
    public String restaurants(Model model,
                              @RequestParam(defaultValue = "10") int pageSize,
                              @RequestParam(defaultValue = "1") int pageNumber,
                              @RequestParam(defaultValue = "name") String sortBy,
                              @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = utilityService.createPagable(pageSize,pageNumber,sortBy,sortDirection);
        Page<Restaurant> restaurantPage = restaurantService.findAll(pageable);
        List<Integer> pageNumbers = utilityService.generatePageNumbers(pageNumber, restaurantPage.getTotalPages());


        model.addAttribute("restaurants", restaurantPage.getContent());
        model.addAttribute("totalPages", restaurantPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("pageNumbers", pageNumbers);
        return "customer-restaurants";
    }



}



