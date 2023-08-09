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
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class RestaurantOrderController {

    public static final String RESTAURANTS_BY_STREET = "/restaurants/street/{street}";
    private static final String RESTAURANT_BY_NAME = "/restaurants/{restaurantName}";
    private static final String RESTAURANT_ADD_ITEMS = "/restaurants/addItems";
    private static final String RESTAURANT_MENU_ITEM_DETAILS = "/restaurants/{restaurantName}/menu/{menuItemId}";
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final MenuItemService menuItemService;
    private final RestaurantMapper restaurantMapper;
    private final AddressMapper addressMapper;
    private final OwnerMapper ownerMapper;
    private final MenuItemOrderMapper menuItemOrderMapper;
    private final MenuItemMapper menuItemMapper;
    private final OrderMapper orderMapper;


    @GetMapping("/address")
    public String showAddressForm(Model model) {
        model.addAttribute("addressDTO", new CustomerAddressOrderDTO());
        return "address-form";
    }

    @PostMapping("/submit-address")
    public String submitAddressForm(@ModelAttribute("addressDTO") CustomerAddressOrderDTO addressDTO,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {

        session.setAttribute("addressDTO", addressDTO);
        redirectAttributes.addAttribute("street", addressDTO.getStreet());
        return "redirect:/restaurants/street/{street}";
    }

    @GetMapping(value = RESTAURANTS_BY_STREET)
    public String showRestaurantsByStreet(@PathVariable String street, Model model) {
        var restaurantsSet = restaurantService.findRestaurantsByStreetName(street).stream()
                .map(restaurantMapper::map).collect(Collectors.toSet());
        model.addAttribute("restaurants", restaurantsSet);
        model.addAttribute("street", street);

        return "restaurants-by-street";
    }

    @GetMapping(RESTAURANT_BY_NAME)
    public String showRestaurantMenu(@PathVariable String restaurantName,
                                     Model model,
                                     HttpSession session) {
        var restaurant = restaurantService.findByName(restaurantName);
        var address = addressMapper.map(restaurant.getAddress());
        var owner = ownerMapper.map(restaurant.getOwner());
        Set<MenuItemDTO> menuItemDTOs = menuItemMapper.map(restaurantService.getAvailableMenuItems(restaurant));
        var menuItems = new ArrayList<>(menuItemDTOs);
        MenuItemOrdersDTO menuItemOrdersDTO = new MenuItemOrdersDTO(menuItems.stream()
                .map(a -> new MenuItemOrderDTO(0, a))
                .toList());
        session.setAttribute("restaurant", restaurantName);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("address", address);
        model.addAttribute("owner", owner);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("menuItemOrdersDTO", menuItemOrdersDTO);

        return "restaurant-menu";
    }

    @GetMapping(RESTAURANT_MENU_ITEM_DETAILS)
    public String showMenuItemDetails(@PathVariable Integer menuItemId,
                                      @PathVariable String restaurantName,
                                      Model model) {

        MenuItemDTO menuItemDTO = menuItemMapper.map(menuItemService.findById(menuItemId));
        String imageBase64 = Base64.getEncoder().encodeToString(menuItemDTO.getImage());
        model.addAttribute("menuItem", menuItemDTO);
        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("imageBase64", imageBase64);
        return "customer-menu-item-details";
    }

    @PostMapping(RESTAURANT_ADD_ITEMS)
    public String addMenuItems(@ModelAttribute MenuItemOrdersDTO menuItemOrdersDTO,
                               HttpSession session) {

        List<MenuItemOrderDTO> menuItemOrderDTOList = menuItemOrdersDTO.getOrders();
        Set<MenuItemOrder> menuItemOrdersToOrder = menuItemOrderDTOList.stream()
                .filter(a -> a.getQuantity() > 0)
                .map(menuItemOrderMapper::map)
                .collect(Collectors.toSet());

        CustomerAddressOrderDTO orderAddressData = (CustomerAddressOrderDTO) session.getAttribute("addressDTO");

        Order order = orderMapper.mapFromDTO(orderAddressData);

        String restaurantName = (String) session.getAttribute("restaurant");

        Order placedOrder = orderService.placeOrder(order, restaurantName, menuItemOrdersToOrder);

        CustomerAddressOrderDTO placed = orderMapper.mapToDTO(placedOrder);

        session.setAttribute("placedOrder", placed);
        session.setAttribute("menuItemOrdersDTO", menuItemOrdersToOrder);

        return "redirect:/thank-you";
    }

    @GetMapping("/thank-you")
    public String orderPlaced(HttpSession session, Model model) {

        CustomerAddressOrderDTO orderDetails = (CustomerAddressOrderDTO) session.getAttribute("placedOrder");
        model.addAttribute("orderDetails", orderDetails);

        Set<MenuItemOrderDTO> menuItemOrdersDTO = (Set<MenuItemOrderDTO>) session.getAttribute("menuItemOrdersDTO");

        model.addAttribute("menuItemOrders", menuItemOrdersDTO);

        return "thank-you";
    }


    private Restaurant getRestaurant(String restaurantName) {
        return restaurantService
                .findByName(restaurantName);

    }

}
