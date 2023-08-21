package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import pl.xavras.FoodOrder.api.dto.mapper.MenuItemMapper;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.business.AddressService;
import pl.xavras.FoodOrder.business.MenuItemService;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.xavras.FoodOrder.api.controller.CustomerRestaurantsController.RESTAURANTS;
import static pl.xavras.FoodOrder.util.DomainFixtures.someRestaurant1;
import static pl.xavras.FoodOrder.util.DomainFixtures.someRestaurant2;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = CustomerRestaurantsController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerRestaurantsControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private MenuItemService menuItemService;
    @MockBean
    private UtilityService utilityService;
    @MockBean
    private final AddressService addressService;
    @MockBean
    private final RestaurantMapper restaurantMapper;
    @MockBean
    private final MenuItemMapper menuItemMapper;



    @Test
    public void testRestaurants() throws Exception {
        //given
        List<Restaurant> restaurants = createRestaurantList();
        Page<Restaurant> restaurantPage = new PageImpl<>(restaurants);
        when(restaurantService.findAll(any())).thenReturn(restaurantPage);

        List<Integer> pageNumbers = createPageNumbersList();
        when(utilityService.generatePageNumbers(anyInt(), anyInt())).thenReturn(pageNumbers);
        //when,then
        mockMvc.perform(get(RESTAURANTS))
                .andExpect(status().isOk())
                .andExpect(view().name("customer-restaurants"))
                .andExpect(model().attribute("restaurants", restaurantPage.getContent()))
                .andExpect(model().attribute("pageNumbers", pageNumbers));

        verify(restaurantService, times(1)).findAll(any());
        verify(utilityService, times(1)).generatePageNumbers(anyInt(), anyInt());
    }

    private List<Restaurant> createRestaurantList() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(someRestaurant1().withName("Restaurant 1"));
        restaurants.add(someRestaurant2().withName("Restaurant 2"));
        return restaurants;
    }

    private List<Integer> createPageNumbersList() {
        List<Integer> pageNumbers = new ArrayList<>();
        pageNumbers.add(1);
        pageNumbers.add(2);
        return pageNumbers;
    }
}