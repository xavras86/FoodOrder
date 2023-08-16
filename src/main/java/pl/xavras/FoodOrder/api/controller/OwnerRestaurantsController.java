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
import pl.xavras.FoodOrder.business.*;
import pl.xavras.FoodOrder.domain.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
@Slf4j
public class OwnerRestaurantsController {

    public static final String RESTAURANT_OWNER = "/owner/restaurants";
    public static final String RESTAURANT_OWNER_ADD_RESTAURANT = "/owner/restaurants/add-restaurant";
    public static final String RESTAURANT_OWNER_NAME = "/owner/restaurants/{restaurantName}";
    public static final String RESTAURANT_OWNER_ADD = "/owner/restaurants/add-menu-item";
    public static final String RESTAURANT_OWNER_EDIT = "owner/restaurants/edit-menu-item/{menuItemId}";
    public static final String RESTAURANT_OWNER_MENU_ITEM_DETAILS = "/owner/restaurants/{restaurantName}/menu/{menuItemId}";
    public static final String RESTAURANT_OWNER_RANGE = "/owner/restaurants/range/{restaurantName}";
    public static final String RESTAURANT_OWNER_RANGE_EDIT = "/owner/restaurants/{restaurantName}/range/{streetId}";
    private final RestaurantService restaurantService;
    private final OwnerService ownerService;
    private final MenuItemService menuItemService;
    private final StreetService streetService;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;
    private final AddressMapper addressMapper;
    private final OwnerMapper ownerMapper;
    private final UtilityService utilityService;


    @GetMapping(RESTAURANT_OWNER)
    public String showRestaurantsByOwner(@RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(defaultValue = "1") int pageNumber,
                                         @RequestParam(defaultValue = "name") String sortBy,
                                         @RequestParam(defaultValue = "asc") String sortDirection,
                                         Model model) {

        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        Owner activeOwner = ownerService.activeOwner();
        Page<Restaurant> restaurantPage = restaurantService.findByOwner(pageable, activeOwner);
        List<Integer> pageNumbers = utilityService.generatePageNumbers(pageNumber, restaurantPage.getTotalPages());




        model.addAttribute("restaurants", restaurantPage.getContent());
        model.addAttribute("totalPages", restaurantPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("pageNumbers", pageNumbers);

        model.addAttribute("restaurantDTO", new RestaurantDTO());
        model.addAttribute("addressDTO", new AddressDTO());
        return "owner-restaurants";
    }

    @PostMapping(RESTAURANT_OWNER_ADD_RESTAURANT)
    public String addNewRestaurant(
            @ModelAttribute("restaurantDTO") RestaurantDTO restaurantDTO,
            @ModelAttribute("addressDTO") AddressDTO addressDTO
    ) {
        Restaurant newRestaurant = restaurantMapper.map(restaurantDTO);
        Address newAddress = addressMapper.map(addressDTO);
        restaurantService.saveNewRestaurant(newRestaurant, newAddress);

        return "redirect:/owner/restaurants";
    }


    @GetMapping(RESTAURANT_OWNER_NAME)
    public String showRestaurantMenu(@PathVariable String restaurantName,
                                     @RequestParam(defaultValue = "10") int pageSize,
                                     @RequestParam(defaultValue = "1") int pageNumber,
                                     @RequestParam(defaultValue = "name") String sortBy,
                                     @RequestParam(defaultValue = "asc") String sortDirection,
                                     Model model
    ) {


        var restaurant = restaurantService.findByName(restaurantName);
        var address = addressMapper.map(restaurant.getAddress());
        var owner = ownerMapper.map(restaurant.getOwner());
//        var menuItems = new ArrayList<>(menuItemMapper.map(restaurant.getMenuItems()));


        Pageable pageable = utilityService.createPagable(pageSize,pageNumber,sortBy,sortDirection);
        Page<MenuItem> menuItemsPage = menuItemService.getMenuItemsByRestaurantPaged(restaurant, pageable);
        List<Integer> pageNumbers = utilityService.generatePageNumbers(pageNumber, menuItemsPage.getTotalPages());

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("address", address);
        model.addAttribute("owner", owner);
        model.addAttribute("newMenuItem", new MenuItemDTO());

        model.addAttribute("menuItems", menuItemsPage.getContent());
        model.addAttribute("totalPages", menuItemsPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("pageNumbers", pageNumbers);

        return "owner-restaurant-details";
    }

    @GetMapping(RESTAURANT_OWNER_MENU_ITEM_DETAILS)
    public String showMenuItemDetails(@PathVariable Integer menuItemId,
                                      @PathVariable String restaurantName,
                                      Model model) {


        MenuItem menuItem = menuItemService.findById(menuItemId);
        String imageBase64 = menuItemService.getString(menuItem);

        model.addAttribute("menuItem", menuItem);
        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("imageBase64", imageBase64);
        return "owner-menu-item-details";
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
        return "redirect:/owner/restaurants/{restaurantName}";
    }

    @PostMapping(RESTAURANT_OWNER_EDIT)
    public String editMenuItem(@PathVariable Integer menuItemId,
                               @RequestParam("restaurantName") String restaurantName,
                               RedirectAttributes redirectAttributes) {

        MenuItem menuItemToEdit = menuItemService.findById(menuItemId);
        menuItemService.changeAvailability(menuItemToEdit);
        redirectAttributes.addAttribute("restaurantName", restaurantName);
        return "redirect:/owner/restaurants/{restaurantName}";
    }

    @GetMapping(RESTAURANT_OWNER_RANGE)
    public String adjustRestaurantRange(@PathVariable String restaurantName,
                                        Model model,
                                        @RequestParam(defaultValue = "10") int pageSize,
                                        @RequestParam(defaultValue = "1") int pageNumber,
                                        @RequestParam(defaultValue = "streetName") String sortBy,
                                        @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        Page<Street> streetPage = streetService.findAll(pageable);
        List<Integer> pageNumbers = utilityService.generatePageNumbers(pageNumber, streetPage.getTotalPages());


        Map<Street, Boolean> streetStatusMap = streetService.createStreetStatusMap(restaurantName, streetPage);

        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("streetStatusMap", streetStatusMap);
        model.addAttribute("totalPages", streetPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("pageNumbers", pageNumbers);

        return "owner-restaurant-delivery-range";
    }


    @PostMapping(RESTAURANT_OWNER_RANGE_EDIT)
    public String editMenuItem(@PathVariable String restaurantName,
                               @PathVariable Integer streetId

    ) {
        Street street = streetService.findById(streetId);

        restaurantService.alternateCoverageStateForStreet(restaurantName, street);

        return "redirect:/owner/restaurants/range/{restaurantName}";
    }


}




