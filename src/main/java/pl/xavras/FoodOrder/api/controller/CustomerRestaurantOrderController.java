package pl.xavras.FoodOrder.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.xavras.FoodOrder.api.dto.*;
import pl.xavras.FoodOrder.api.dto.mapper.*;
import pl.xavras.FoodOrder.business.*;
import pl.xavras.FoodOrder.domain.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class  CustomerRestaurantOrderController {

    public static final String RESTAURANTS_BY_STREET = "/restaurants/street/{street}";
    private static final String RESTAURANT_BY_NAME = "/restaurants/{restaurantName}";
    private static final String RESTAURANT_ADD_ITEMS = "/restaurants/addItems";
    private static final String RESTAURANT_MENU_ITEM_DETAILS = "/restaurants/{restaurantName}/menu/{menuItemId}";
    private static final String ORDER_DETAILS = "/order/thanks/{orderNumber}";
    private final RestaurantService restaurantService;
    private final OrderService orderService;

    private final StreetService streetService;
    private final MenuItemService menuItemService;
    private final RestaurantMapper restaurantMapper;
    private final AddressMapper addressMapper;
    private final OwnerMapper ownerMapper;
    private final MenuItemOrderMapper menuItemOrderMapper;
    private final MenuItemMapper menuItemMapper;
    private final OrderMapper orderMapper;

    private final AddressService addressService;
    private final StreetMapper streetMapper;


    @GetMapping("/address")
    public String showAddressForm(Model model) {
        var all =  streetMapper.map(streetService.findAll());
        List<String> allCountries = List.of("Polska");
        List<String> allCities = List.of("Poznań");
        List<String> allStreets = all.stream().map(StreetDTO::getStreetName).toList();
        model.addAttribute("addressDTO", new CustomerAddressOrderDTO());
        model.addAttribute("countries", allCountries);
        model.addAttribute("cities", allCities);
        model.addAttribute("streets", allStreets);
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

        //wyciagnac do metory
        String imageBase64 = getString(menuItemDTO);

        model.addAttribute("menuItem", menuItemDTO);
        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("imageBase64", imageBase64);
        return "customer-menu-item-details";
    }

    private String getString(MenuItemDTO menuItemDTO) {
        String imageBase64 = null;
        if (menuItemDTO.getImage() != null) {
            imageBase64 = Base64.getEncoder().encodeToString(menuItemDTO.getImage());
        }
        return imageBase64;
    }

    @PostMapping(RESTAURANT_ADD_ITEMS)
    public String addMenuItems(@ModelAttribute MenuItemOrdersDTO menuItemOrdersDTO,
                               HttpSession session,
                               RedirectAttributes redirectAttributes
                               ) {

        List<MenuItemOrderDTO> menuItemOrderDTOList = menuItemOrdersDTO.getOrders();
        Set<MenuItemOrder> menuItemOrdersToOrder = new HashSet<>();
        if(Objects.nonNull(menuItemOrderDTOList)) {
            menuItemOrdersToOrder = menuItemOrderDTOList.stream()
                    .filter(a -> a.getQuantity() > 0)
                    .map(menuItemOrderMapper::map)
                    .collect(Collectors.toSet());
        }

        CustomerAddressOrderDTO orderAddressData = (CustomerAddressOrderDTO) session.getAttribute("addressDTO");

        Order order = orderMapper.mapFromDTO(orderAddressData);

        String restaurantName = (String) session.getAttribute("restaurant");

        Order placedOrder = orderService.placeOrder(order, restaurantName, menuItemOrdersToOrder);
        String orderNumber = placedOrder.getOrderNumber();

        CustomerAddressOrderDTO placed = orderMapper.mapToDTO(placedOrder);

        session.setAttribute("placedOrder", placed);
        session.setAttribute("orderNumber", orderNumber);

        session.setAttribute("menuItemOrdersDTO", menuItemOrdersToOrder);
        redirectAttributes.addAttribute("orderNumber", orderNumber);

        return "redirect:/order/thanks/{orderNumber}";
    }

    @GetMapping(ORDER_DETAILS)
    public String orderPlaced(@PathVariable String orderNumber,
                              Model model) {

        Order order = orderService.findByOrderNumber(orderNumber);
        Address deliveryAddress = order.getAddress();
        Address restaurantAddress = order.getRestaurant().getAddress();
        Set<MenuItemOrder> menuItemOrders = order.getMenuItemOrders();
        String status = orderService.orderStatus(order);
        String mapUrl = addressService.createMapUrl(restaurantAddress, deliveryAddress);

        model.addAttribute("order", order);
        model.addAttribute("deliveryAddress", deliveryAddress);
        model.addAttribute("restaurantAddress", restaurantAddress);
        model.addAttribute("menuItemOrders", menuItemOrders);
        model.addAttribute("status", status);
        model.addAttribute("mapUrl",  mapUrl);

        return "customer-thank-you";
    }



    private Restaurant getRestaurant(String restaurantName) {
        return restaurantService
                .findByName(restaurantName);

    }

}
