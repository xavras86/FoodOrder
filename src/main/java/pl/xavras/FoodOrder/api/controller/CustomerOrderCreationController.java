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
import pl.xavras.FoodOrder.business.AddressService;
import pl.xavras.FoodOrder.business.MenuItemService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.StreetService;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class CustomerOrderCreationController {

    public static final String NO_ADDRESS_NOTIFY = "It looks like you haven't given us a delivery address yet, please complete it so we can tell you which restaurants deliver to your address.";
    public static final String NO_ITEMS_IN_ORDER = "Your order does not contain any items, please try again.";
    public static final String ADDRESS_OUT_OF_RANGE = "Unfortunately, we cannot accept your order as the selected restaurant does not deliver food to the address you've chosen. Please select a restaurant from the list of available options.";
    private static final String RESTAURANT_ADD_ITEMS = "/customer/restaurants/addItems/{restaurantName}";
    private static final String DELIVERY_ADDRESS = "/customer/address";
    private static final String RESTAURANT_MENU_ITEM_DETAILS = "/customer/restaurants/{restaurantName}/menu/{menuItemId}";
    private static final String ORDER_THANKS = "/customer/orders/thanks/{orderNumber}";
    private static final String SUBMIT_ADDRESS = "/customer/submit-address";
    private final OrderService orderService;

    private final StreetService streetService;
    private final MenuItemService menuItemService;

    private final AddressMapper addressMapper;
    private final MenuItemOrderMapper menuItemOrderMapper;
    private final MenuItemMapper menuItemMapper;
    private final OrderMapper orderMapper;

    private final AddressService addressService;
    private final StreetMapper streetMapper;


    @GetMapping(DELIVERY_ADDRESS)
    public String showAddressForm(Model model) {
        var all = streetMapper.map(streetService.findAll());
        List<String> allCountries = List.of("Polska"); //ograniczamy zasieg działania ;)
        List<String> allCities = List.of("Poznań"); //ograniczamy zasieg działania ;)
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

    @PostMapping(RESTAURANT_ADD_ITEMS)
    public String addMenuItems(@PathVariable String restaurantName,
                               @ModelAttribute MenuItemOrdersDTO menuItemOrdersDTO,
                               HttpSession session,
                               RedirectAttributes redirectAttributes
    ) {

        List<MenuItemOrderDTO> menuItemOrderDTOList = menuItemOrdersDTO.getOrders();

        Set<MenuItemOrder> menuItemOrdersToOrder = getMenuItemOrders(menuItemOrderDTOList);

        //czy pusty "koszyk"
        if (menuItemOrdersToOrder.isEmpty()) {
            redirectAttributes.addAttribute("restaurantName", restaurantName);
            redirectAttributes.addFlashAttribute("noItemsOrdered", NO_ITEMS_IN_ORDER);
            return "redirect:/customer/restaurants/{restaurantName}";
        }
        AddressDTO deliveryAddressDTO = (AddressDTO) session.getAttribute("addressDTO");

        //czy brak podanego adresu
        if (Objects.isNull(deliveryAddressDTO)) {
            redirectAttributes.addFlashAttribute("messageNoAddress", NO_ADDRESS_NOTIFY);
            return "redirect:/customer/address";
        }
        Address deliveryAddress = addressMapper.map(deliveryAddressDTO);
        //address poza zasięgiem
        if(!streetService.isDeliveryStreetInRange(restaurantName, deliveryAddress)) {
            redirectAttributes.addAttribute("street", deliveryAddressDTO.getStreet());
            redirectAttributes.addFlashAttribute("addressOutOfRange", ADDRESS_OUT_OF_RANGE);
            return "redirect:/customer/restaurants/street/{street}";
        }


        Order placedOrder = orderService.placeOrder(deliveryAddress, restaurantName, menuItemOrdersToOrder);
        String orderNumber = placedOrder.getOrderNumber();

        redirectAttributes.addAttribute("orderNumber", orderNumber);

        return "redirect:/customer/orders/thanks/{orderNumber}";

    }

    private Set<MenuItemOrder> getMenuItemOrders(List<MenuItemOrderDTO> menuItemOrderDTOList) {
        if(Objects.nonNull(menuItemOrderDTOList)) {
            return menuItemOrderDTOList.stream()
                    .filter(a -> a.getQuantity() > 0)
                    .map(menuItemOrderMapper::map)
                    .collect(Collectors.toSet());
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
        Address restaurantAddress = order.getAddress();
        Address deliveryAddress = order.getRestaurant().getAddress();
        Set<MenuItemOrder> menuItemOrders = orderService.findMenuItemOrdersByOrder(order);

        String status = orderService.orderStatus(order);
        String mapUrl = addressService.createMapUrl(restaurantAddress, deliveryAddress);


        model.addAttribute("order", orderDTO);
        model.addAttribute("deliveryAddress",  addressMapper.map(restaurantAddress));
        model.addAttribute("restaurantAddress", addressMapper.map(deliveryAddress));
        model.addAttribute("menuItemOrders", menuItemOrders);
        model.addAttribute("status", status);
        model.addAttribute("mapUrl", mapUrl);

        return "customer-thank-you";
    }

}
