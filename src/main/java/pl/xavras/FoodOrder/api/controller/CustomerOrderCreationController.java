package pl.xavras.FoodOrder.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.xavras.FoodOrder.api.dto.*;
import pl.xavras.FoodOrder.api.dto.mapper.*;
import pl.xavras.FoodOrder.business.*;
import pl.xavras.FoodOrder.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class CustomerOrderCreationController {


    public static final String RESTAURANTS_BY_STREET = "/customer/restaurants/street/{street}";
    public static final String NO_ADDRESS_NOTIFY = "It looks like you haven't given us a delivery address yet, please complete it so we can tell you which restaurants deliver to your address.";
    public static final String NO_ITEMS_IN_ORDER = "Your order does not contain any items, please try again.";
    private static final String RESTAURANT_BY_NAME = "/customer/restaurants/{restaurantName}";
    private static final String RESTAURANT_ADD_ITEMS = "/customer/restaurants/addItems/{restaurantName}";
    private static final String DELIVERY_ADDRESS = "/customer/address";
    private static final String RESTAURANT_MENU_ITEM_DETAILS = "/customer/restaurants/{restaurantName}/menu/{menuItemId}";
    private static final String ORDER_THANKS = "/customer/orders/thanks/{orderNumber}";
    private static final String SUBMIT_ADDRESS = "/customer/submit-address";
    private final RestaurantService restaurantService;
    private final OrderService orderService;

    private final StreetService streetService;
    private final MenuItemService menuItemService;

    private final UtilityService utilityService;
    private final RestaurantMapper restaurantMapper;
    private final AddressMapper addressMapper;
    private final OwnerMapper ownerMapper;
    private final MenuItemOrderMapper menuItemOrderMapper;
    private final MenuItemMapper menuItemMapper;
    private final OrderMapper orderMapper;

    private final AddressService addressService;
    private final StreetMapper streetMapper;


    @GetMapping(DELIVERY_ADDRESS)
    public String showAddressForm(Model model) {
        var all = streetMapper.map(streetService.findAll());
        List<String> allCountries = List.of("Polska");
        List<String> allCities = List.of("Poznań");
        List<String> allStreets = all.stream().map(StreetDTO::getStreetName).toList();
        model.addAttribute("addressDTO", new AddressDTO());
        model.addAttribute("countries", allCountries);
        model.addAttribute("cities", allCities);
        model.addAttribute("streets", allStreets);
        return "customer-address-form";

    }

    @PostMapping(SUBMIT_ADDRESS)
    public String submitAddressForm(@ModelAttribute("addressDTO") AddressDTO addressDTO,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {

        session.setAttribute("addressDTO", addressDTO);
        redirectAttributes.addAttribute("street", addressDTO.getStreet());
        return "redirect:/customer/restaurants/street/{street}";
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
        String attributeValue = "AIzaSyDvwTDMi-cXeamefAssYo3ZmhtHXnnZO9g";
        model.addAttribute("apiKey", attributeValue);
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
//todo refactor!
        var restaurant = restaurantService.findByName(restaurantName);
        var addressDTO = addressMapper.map(restaurant.getAddress());
        var owner = ownerMapper.map(restaurant.getOwner());

        Pageable pageable = utilityService.createPagable(pageSize, pageNumber, sortBy, sortDirection);
        Page<MenuItemDTO> menuItemsPage = menuItemService.getAvailableMenuItemsByRestaurant(restaurant, pageable)
                .map(menuItemMapper::map);
        List<Integer> pageNumbers = utilityService.generatePageNumbers(pageNumber, menuItemsPage.getTotalPages());

        MenuItemOrdersDTO menuItemOrdersDTO = new MenuItemOrdersDTO(menuItemsPage.stream()
                .map(a -> new MenuItemOrderDTO(0, a))
                .toList());

        String mapUrl = addressService.createMapUrlPoint(addressDTO);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("address", addressDTO);
        model.addAttribute("owner", owner);
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


    @PostMapping(RESTAURANT_ADD_ITEMS)
    public String addMenuItems(@PathVariable String restaurantName,
                               @ModelAttribute MenuItemOrdersDTO menuItemOrdersDTO,
                               HttpSession session,
                               RedirectAttributes redirectAttributes
    ) {

        List<MenuItemOrderDTO> menuItemOrderDTOList = menuItemOrdersDTO.getOrders();


        Set<MenuItemOrder> menuItemOrdersToOrder = getMenuItemOrders(menuItemOrderDTOList);

        if (menuItemOrdersToOrder.isEmpty()) {
            redirectAttributes.addAttribute("restaurantName", restaurantName);
            redirectAttributes.addFlashAttribute("noItemsOrdered", NO_ITEMS_IN_ORDER);
            return "redirect:/customer/restaurants/{restaurantName}";
        }
        if (Objects.isNull(session.getAttribute("addressDTO"))) {

            redirectAttributes.addFlashAttribute("messageNoAddress", NO_ADDRESS_NOTIFY);
            return "redirect:/customer/address";
        }

        AddressDTO orderAddressData = (AddressDTO) session.getAttribute("addressDTO");

        Address deliveryAddress = addressMapper.map(orderAddressData);

        Order placedOrder = orderService.placeOrder(deliveryAddress, restaurantName, menuItemOrdersToOrder);
        String orderNumber = placedOrder.getOrderNumber();

//        OrderDTO placed = orderMapper.mapToDTO(placedOrder);
//        session.setAttribute("placedOrder", placed);
//        session.setAttribute("orderNumber", orderNumber);

        redirectAttributes.addAttribute("orderNumber", orderNumber);

        return "redirect:/customer/orders/thanks/{orderNumber}";

    }

    private Set<MenuItemOrder> getMenuItemOrders(List<MenuItemOrderDTO> menuItemOrderDTOList) {
        if(Objects.nonNull(menuItemOrderDTOList)) {
            Set<MenuItemOrder> menuItemOrdersToOrder = menuItemOrderDTOList.stream()
                    .filter(a -> a.getQuantity() > 0)
                    .map(menuItemOrderMapper::map)
                    .collect(Collectors.toSet());
            return menuItemOrdersToOrder;
        }else return Collections.emptySet();
    }

    @GetMapping(RESTAURANT_MENU_ITEM_DETAILS)
    public String showMenuItemDetails(@PathVariable Integer menuItemId,
                                      @PathVariable String restaurantName,
                                      Model model) {

        MenuItem menuItem = menuItemService.findById(menuItemId);
        String imageBase64 = menuItemService.getString(menuItem);
        MenuItemDTO menuItemDTO = menuItemMapper.map(menuItem);


        model.addAttribute("menuItem", menuItemDTO);
        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("imageBase64", imageBase64);
        return "customer-menu-item-details";
    }


    @GetMapping(ORDER_THANKS)

    public String orderPlaced(@PathVariable String orderNumber,
                              Model model) {

        Order order = orderService.findByOrderNumber(orderNumber);
        OrderDTO orderDTO = orderMapper.mapToDTO(order);
        AddressDTO deliveryAddressDTO = orderDTO.getAddress();
        AddressDTO restaurantAddressDTO = addressMapper.map(order.getRestaurant().getAddress());
        Set<MenuItemOrder> menuItemOrders = order.getMenuItemOrders();
        String status = orderService.orderStatus(order);
        String mapUrl = addressService.createMapUrl(restaurantAddressDTO, deliveryAddressDTO);

        model.addAttribute("order", orderDTO);
        model.addAttribute("deliveryAddress", deliveryAddressDTO);
        model.addAttribute("restaurantAddress", restaurantAddressDTO);
        model.addAttribute("menuItemOrders", menuItemOrders);
        model.addAttribute("status", status);
        model.addAttribute("mapUrl", mapUrl);

        return "customer-thank-you";
    }

    private Restaurant getRestaurant(String restaurantName) {
        return restaurantService
                .findByName(restaurantName);
    }


}
