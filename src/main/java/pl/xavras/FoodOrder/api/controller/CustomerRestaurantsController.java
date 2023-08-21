package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.xavras.FoodOrder.api.dto.MenuItemDTO;
import pl.xavras.FoodOrder.api.dto.MenuItemOrderDTO;
import pl.xavras.FoodOrder.api.dto.MenuItemOrdersDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.mapper.MenuItemMapper;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.business.AddressService;
import pl.xavras.FoodOrder.business.MenuItemService;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.List;


@Controller
@AllArgsConstructor
@Slf4j
public class CustomerRestaurantsController {

    public static final String RESTAURANTS = "/customer/restaurants";
    public static final String RESTAURANTS_BY_STREET = "/customer/restaurants/street/{street}";
    private static final String RESTAURANT_BY_NAME = "/customer/restaurants/{restaurantName}";
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final UtilityService utilityService;
    private final AddressService addressService;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;


    @GetMapping(RESTAURANTS)
    public String restaurants(Model model,
                              @RequestParam(defaultValue = "10") int pageSize,
                              @RequestParam(defaultValue = "1") int pageNumber,
                              @RequestParam(defaultValue = "name") String sortBy,
                              @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = utilityService.createPagable(pageSize, pageNumber, sortBy, sortDirection);
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

    @GetMapping(RESTAURANTS_BY_STREET)
    public String showRestaurantsByStreet(@PathVariable String street,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(defaultValue = "1") int pageNumber,
                                          @RequestParam(defaultValue = "name") String sortBy,
                                          @RequestParam(defaultValue = "asc") String sortDirection,
                                          Model model) {

        Pageable pageable = utilityService.createPagable(pageSize, pageNumber, sortBy, sortDirection);
        Page<Restaurant> restaurantsByStreetNamePaged = restaurantService.findRestaurantsByStreetNamePaged(street, pageable);
        Page<RestaurantDTO> restaurantPage = restaurantsByStreetNamePaged
                .map(restaurantMapper::map);

        List<Integer> pageNumbers = utilityService.generatePageNumbers(pageNumber, restaurantPage.getTotalPages());

        List<Address> addresses = restaurantsByStreetNamePaged.map(Restaurant::getAddress).stream().toList();


        model.addAttribute("restaurants", restaurantPage.getContent());
        model.addAttribute("addresses", addresses);
        model.addAttribute("street", street);
        model.addAttribute("totalPages", restaurantPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("pageNumbers", pageNumbers);

        return "customer-restaurants-by-street";
    }

    @GetMapping(RESTAURANT_BY_NAME)
    public String showRestaurantMenu(@PathVariable String restaurantName,
                                     @RequestParam(defaultValue = "10") int pageSize,
                                     @RequestParam(defaultValue = "1") int pageNumber,
                                     @RequestParam(defaultValue = "name") String sortBy,
                                     @RequestParam(defaultValue = "asc") String sortDirection,
                                     Model model) {

        var restaurant = restaurantService.findByName(restaurantName);
        RestaurantDTO restaurantDTO = restaurantMapper.map(restaurant);

        Pageable pageable = utilityService.createPagable(pageSize, pageNumber, sortBy, sortDirection);
        Page<MenuItemDTO> menuItemsPage = menuItemService.getAvailableMenuItemsByRestaurant(restaurant, pageable)
                .map(menuItemMapper::map);
        List<Integer> pageNumbers = utilityService.generatePageNumbers(pageNumber, menuItemsPage.getTotalPages());

        MenuItemOrdersDTO menuItemOrdersDTO = new MenuItemOrdersDTO(menuItemsPage.stream()
                .map(a -> new MenuItemOrderDTO(0, a))
                .toList());

        String mapUrl = addressService.createMapUrlPoint(restaurant.getAddress());

        model.addAttribute("restaurant", restaurantDTO);
        model.addAttribute("menuItems", menuItemsPage.getContent());
        model.addAttribute("menuItemOrdersDTO", menuItemOrdersDTO);
        model.addAttribute("totalPages", menuItemsPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("mapUrl", mapUrl);

        return "customer-restaurant-menu";
    }

}



