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
import pl.xavras.FoodOrder.api.dto.*;
import pl.xavras.FoodOrder.api.dto.mapper.*;
import pl.xavras.FoodOrder.business.*;
import pl.xavras.FoodOrder.domain.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
@Slf4j
public class OwnerRestaurantController {

    public static final String RESTAURANT_OWNER = "/owner/restaurants";
    public static final String RESTAURANT_OWNER_ADD_RESTAURANT = "/restaurants/owner/add-restaurant";
    public static final String RESTAURANT_OWNER_NAME = "/restaurants/owner/{restaurantName}";
    public static final String RESTAURANT_OWNER_ADD = "/restaurants/owner/add-menu-item";
    public static final String RESTAURANT_OWNER_EDIT = "/restaurants/owner-edit/{menuItemId}";
    public static final String RESTAURANT_OWNER_MENU_ITEM_DETAILS = "/restaurants/owner/{restaurantName}/menu/{menuItemId}";
    public static final String RESTAURANT_OWNER_RANGE = "/restaurants/owner/range/{restaurantName}";
    public static final String RESTAURANT_OWNER_RANGE_EDIT = "/restaurants/owner/{restaurantName}/range/{streetId}";
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
    public String showRestaurantsByOwner(Model model) {

        String email = ownerService.activeOwner().getEmail();
        Set<RestaurantDTO> restaurantByOwner = restaurantService.findByOwnerEmail(email).stream()
                .map(restaurantMapper::map).collect(Collectors.toSet());
        model.addAttribute("ownerEmail", email);
        model.addAttribute("restaurantByOwner", restaurantByOwner);
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

        return "owner-restaurant-details";
    }

    @GetMapping(RESTAURANT_OWNER_MENU_ITEM_DETAILS)
    public String showMenuItemDetails(@PathVariable Integer menuItemId,
                                      @PathVariable String restaurantName,
                                      Model model) {


        MenuItemDTO menuItemDTO = menuItemMapper.map(menuItemService.findById(menuItemId));

        //wyciagnac do metody w servisie
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

        MenuItem menuItemToEdit = menuItemService.findById(menuItemId);
        menuItemService.changeAvailability(menuItemToEdit);
        redirectAttributes.addAttribute("restaurantName", restaurantName);
        return "redirect:/restaurants/owner/{restaurantName}";
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


//
        Map<Street, Boolean> streetStatusMap = streetPage.stream()
                .collect(Collectors.toMap(
                        street -> street,
                        street -> restaurantService.checkStreetCoverageForRestaurant(restaurantName, street),
                        (existingValue, newValue) -> existingValue,
                        LinkedHashMap::new
                ));

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




    @PutMapping(RESTAURANT_OWNER_RANGE_EDIT)
    public String editMenuItem(@PathVariable String restaurantName,
                               @PathVariable Integer streetId

    ) {
        Restaurant restaurant = restaurantService.findByName(restaurantName);
        Street street = streetService.findById(streetId);

        restaurantService.alternateCoverageStateForStreet(restaurantName, street);

        return "redirect:/restaurants/owner/range/{restaurantName}";
    }


}




