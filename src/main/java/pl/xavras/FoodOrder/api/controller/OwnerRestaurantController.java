package pl.xavras.FoodOrder.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.xavras.FoodOrder.api.dto.*;
import pl.xavras.FoodOrder.api.dto.mapper.*;
import pl.xavras.FoodOrder.business.MenuItemService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.OwnerService;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
@Slf4j
public class OwnerRestaurantController {
    private final RestaurantService restaurantService;
    private final OwnerService ownerService;
    private final MenuItemService menuItemService;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;
    private final AddressMapper addressMapper;
    private final OwnerMapper ownerMapper;



    @GetMapping("/restaurants-owner")
    public String showRestaurantsByOwner(Model model) {

        String email = ownerService.activeOwner().getEmail();
        Set<RestaurantDTO> restaurantByOwner = restaurantService.findByOwnerEmail(email).stream()
                .map(restaurantMapper::map).collect(Collectors.toSet());
        model.addAttribute("ownerEmail", email);
        model.addAttribute("restaurantByOwner", restaurantByOwner);
        return "restaurants-by-owner";
    }

    @GetMapping("/restaurants/owner/{restaurantName}")
    public String showRestaurantMenu(@PathVariable String restaurantName,
                                     Model model
    ) {
        var restaurant = restaurantService.findByName(restaurantName);
        var address = addressMapper.map(restaurant.getAddress());
        var owner = ownerMapper.map(restaurant.getOwner());
        var menuItems = new ArrayList<>(menuItemMapper.map(restaurant.getMenuItems()));

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("address", address);
        model.addAttribute("owner", owner);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("newMenuItem", new MenuItemDTO());

        return "restaurant-menu-owner";
    }

    @PostMapping("/restaurants/owner-add")
    public String addPositionToMenu(
            @ModelAttribute("newMenuItem") MenuItemDTO menuItemDTO,
            @RequestParam("restaurantName") String restaurantName,
            RedirectAttributes redirectAttributes

    ) {
        Restaurant restaurant = restaurantService.findByName(restaurantName);
        MenuItem menuItemToSave = menuItemMapper.map(menuItemDTO);
        menuItemService.saveMenuItem(menuItemToSave, restaurant);

        redirectAttributes.addAttribute("restaurantName", restaurant.getName());
        return "redirect:/restaurants/owner/{restaurantName}";
    }

}




