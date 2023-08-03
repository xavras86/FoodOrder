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
import pl.xavras.FoodOrder.api.dto.CustomerAddressOrderDTO;
import pl.xavras.FoodOrder.api.dto.MenuItemDTO;
import pl.xavras.FoodOrder.api.dto.MenuItemOrderDTO;
import pl.xavras.FoodOrder.api.dto.MenuItemOrdersDTO;
import pl.xavras.FoodOrder.api.dto.mapper.*;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class NewRestaurantController {

    public static final String RESTAURANTS_BY_STREET = "/restaurants/street/{street}";
    private static final String RESTAURANT_BY_NAME = "/restaurants/{restaurantName}";
    private static final String RESTAURANT_ADDITEMS = "/restaurants/addItems";

    private final RestaurantService restaurantService;

    private final OrderService orderService;

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

        return "restaurantsByStreet";
    }

    @GetMapping("/menu-item-form")
    public String showMenuItemForm(Model model) {
        model.addAttribute("menuItemOrdersDTO", new MenuItemOrdersDTO());
        // Dodaj listę dostępnych pozycji menu (MenuItemDTO) do modelu, np. z bazy danych lub statycznie
        // model.addAttribute("menuItems", listaMenuItems);
        return "menu-item-form";
    }

    @PostMapping("/menu-item-form")
    public String submitMenuItemForm(@ModelAttribute("menuItemOrdersDTO") MenuItemOrdersDTO menuItemOrdersDTO, HttpSession session) {
        // Obsłuż dane z formularza dla pozycji menu
        // Możesz np. zapisać zamówienie w bazie danych lub przekazać do kolejnego etapu przetwarzania
        // Zapisz menuItemOrdersDTO do sesji, aby zachować je między formularzami
        session.setAttribute("menuItemOrdersDTO", menuItemOrdersDTO);
        return "redirect:/thank-you"; // lub inna strona po złożeniu zamówienia
    }


    @GetMapping(RESTAURANT_BY_NAME)
    public String showRestaurantDetails(@PathVariable String restaurantName, Model model) {
        var restaurant = getRestaurant(restaurantName);

        var address = addressMapper.map(restaurant.getAddress());
        var owner = ownerMapper.map(restaurant.getOwner());
        var restaurantDetails = restaurantMapper.map(restaurant);
        var menuItems = new ArrayList<>(getMenuItemDTOs(restaurant));

        MenuItemOrdersDTO menuItemOrdersDTO = new MenuItemOrdersDTO(menuItems.stream().map(a -> new MenuItemOrderDTO(0, a)).toList());

        model.addAttribute("restaurant", restaurantDetails);
        model.addAttribute("address", address);
        model.addAttribute("owner", owner);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("menuItemOrdersDTO", menuItemOrdersDTO);
        model.addAttribute("orderDataDTO", new CustomerAddressOrderDTO());
        model.addAttribute("restaurantName", restaurantName);

        return "restaurantDetails";
    }

    @PostMapping(RESTAURANT_ADDITEMS)
    public String addMenuItems(@ModelAttribute MenuItemOrdersDTO menuItemOrdersDTO,
//                               @ModelAttribute("restaurantName") String name,
//                               Model model,
                               HttpSession session) {


        List<MenuItemOrderDTO> menuItemOrderDTOList = menuItemOrdersDTO.getOrders();
        Set<MenuItemOrder> menuItemOrdersToOrder = menuItemOrderDTOList.stream()
                .filter(a -> a.getQuantity() > 0)
                .map(menuItemOrderMapper::map)
                .collect(Collectors.toSet());

        CustomerAddressOrderDTO orderAddressData = (CustomerAddressOrderDTO) session.getAttribute("addressDTO");




        Order order = orderMapper.mapFromDTO(orderAddressData);
        Order order1 = orderService.placeOrder(order, menuItemOrdersToOrder);
        



        return "thank-you";
    }



    private Set<MenuItemDTO> getMenuItemDTOs(Restaurant restaurant) {
        return menuItemMapper.map(restaurant.getMenuItems());
    }

    private Restaurant getRestaurant(String restaurantName) {
        return restaurantService
                .findByName(restaurantName);

    }



    @GetMapping("/thank-you")
    public String showThankYouPage(HttpSession session, Model model) {
        // Pobierz dane z sesji
//        CustomerAddressOrderDTO addressDTO = (CustomerAddressOrderDTO) session.getAttribute("addressDTO");
//        MenuItemOrdersDTO menuItemOrdersDTO = (MenuItemOrdersDTO) session.getAttribute("menuItemOrdersDTO");
//
//        // Tworzenie obiektu OrderDTO i przekazanie do widoku
//        OrderDTO orderDTO = new OrderDTO();
//        orderDTO.setCustomerAddressOrderDTO(addressDTO);
//        orderDTO.setMenuItemOrdersDTO(menuItemOrdersDTO);
//
//        // Tutaj możesz wykonać dodatkowe operacje, np. obliczenia sumy zamówienia, itp.
//
//        model.addAttribute("orderDTO", orderDTO);
        return "thank-you"; // Wyświetl stronę z podsumowaniem zamówienia
    }

}
