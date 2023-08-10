package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.xavras.FoodOrder.api.dto.AddressDTO;
import pl.xavras.FoodOrder.api.dto.MenuItemDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.mapper.AddressMapper;
import pl.xavras.FoodOrder.api.dto.mapper.MenuItemMapper;
import pl.xavras.FoodOrder.api.dto.mapper.OwnerMapper;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.business.MenuItemService;
import pl.xavras.FoodOrder.business.OwnerService;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
@Slf4j
public class OwnerRestaurantController {

    public static final String RESTAURANT_OWNER = "/restaurants/owner";
    public static final String RESTAURANT_OWNER_ADD_RESTAURANT = "/restaurants/owner/add-restaurant";
    public static final String RESTAURANT_OWNER_NAME = "/restaurants/owner/{restaurantName}";
    public static final String RESTAURANT_OWNER_ADD = "/restaurants/owner/add-menu-item";
    public static final String RESTAURANT_OWNER_EDIT = "/restaurants/owner-edit/{menuItemId}";
    public static final String RESTAURANT_OWNER_MENU_ITEM_DETAILS = "/restaurants/owner/{restaurantName}/menu/{menuItemId}";
    private final RestaurantService restaurantService;
    private final OwnerService ownerService;
    private final MenuItemService menuItemService;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;
    private final AddressMapper addressMapper;
    private final OwnerMapper ownerMapper;


    @GetMapping(RESTAURANT_OWNER)
    public String showRestaurantsByOwner(Model model) {

        String email = ownerService.activeOwner().getEmail();
        Set<RestaurantDTO> restaurantByOwner = restaurantService.findByOwnerEmail(email).stream()
                .map(restaurantMapper::map).collect(Collectors.toSet());
        model.addAttribute("ownerEmail", email);
        model.addAttribute("restaurantByOwner", restaurantByOwner);
        model.addAttribute("restaurantDTO", new RestaurantDTO());
        model.addAttribute("addressDTO", new AddressDTO());
        return "restaurants-by-owner";
    }

    @PostMapping(RESTAURANT_OWNER_ADD_RESTAURANT)
    public String addNewRestaurant(
            @ModelAttribute("restaurantDTO") RestaurantDTO restaurantDTO,
            @ModelAttribute("addressDTO") AddressDTO addressDTO
    ) {
        Restaurant newRestaurant = restaurantMapper.map(restaurantDTO);
        Address newAddress = addressMapper.map(addressDTO);
        restaurantService.saveNewRestaurant(newRestaurant, newAddress);

        return "redirect:/restaurants/owner";
    }


    @GetMapping(RESTAURANT_OWNER_NAME)
    public String showRestaurantMenu(@PathVariable String restaurantName,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(name = "sortField", defaultValue = "name") String sortField,
                                     @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection,
                                     Model model

    ) {


        var restaurant = restaurantService.findByName(restaurantName);
        var address = addressMapper.map(restaurant.getAddress());
        var owner = ownerMapper.map(restaurant.getOwner());
        var menuItems = new ArrayList<>(menuItemMapper.map(restaurant.getMenuItems()));

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("PAGEABLE: " + pageable.toString());

        Page<MenuItem> menuItemsPage = menuItemService.getMenuItemsByRestaurantPaged(restaurant, pageable);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("address", address);
        model.addAttribute("owner", owner);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("newMenuItem", new MenuItemDTO());

        model.addAttribute("size", size);
        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("menuItemsPage", menuItemsPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);

        return "restaurant-menu-owner";
    }

    @GetMapping(RESTAURANT_OWNER_MENU_ITEM_DETAILS)
    public String showMenuItemDetails(@PathVariable Integer menuItemId,
                                      @PathVariable String restaurantName,
                                      Model model) {


        MenuItemDTO menuItemDTO = menuItemMapper.map(menuItemService.findById(menuItemId));

        //wyciagnac do metory
        String imageBase64 = getString(menuItemDTO);

        model.addAttribute("menuItem", menuItemDTO);
        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("imageBase64", imageBase64);
        return "owner-menu-item-details";
    }

    private String getString(MenuItemDTO menuItemDTO) {
        String imageBase64 = null;
        if (menuItemDTO.getImage() != null) {
            imageBase64 = Base64.getEncoder().encodeToString(menuItemDTO.getImage());
        }
        return imageBase64;
    }


    @PostMapping(RESTAURANT_OWNER_ADD)
    public String editMenuItem(
            @ModelAttribute("newMenuItem") MenuItemDTO menuItemDTO,
            @RequestParam("restaurantName") String restaurantName,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        Restaurant restaurant = restaurantService.findByName(restaurantName);

        byte[] imageBytes = imageFile.getBytes();
        menuItemDTO.setImage(imageBytes);

        MenuItem menuItemToSave = menuItemMapper.map(menuItemDTO).withAvailable(true);


        menuItemService.saveMenuItem(menuItemToSave, restaurant);

        redirectAttributes.addAttribute("restaurantName", restaurant.getName());
        return "redirect:/restaurants/owner/{restaurantName}";
    }

    @PutMapping(RESTAURANT_OWNER_EDIT)
    public String editMenuItem(@PathVariable Integer menuItemId,
                               @RequestParam("restaurantName") String restaurantName,
                               RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.findByName(restaurantName);
        MenuItem menuItemToEdit = menuItemService.findById(menuItemId);


        menuItemService.changeAvailability(menuItemToEdit);

        redirectAttributes.addAttribute("restaurantName", restaurant.getName());
        return "redirect:/restaurants/owner/{restaurantName}";
    }
}




