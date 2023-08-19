package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.xavras.FoodOrder.api.dto.AddressDTO;
import pl.xavras.FoodOrder.api.dto.MenuItemOrdersDTO;
import pl.xavras.FoodOrder.api.dto.mapper.*;
import pl.xavras.FoodOrder.business.*;
import pl.xavras.FoodOrder.domain.Street;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.xavras.FoodOrder.util.DomainFixtures.someStreet1;
import static pl.xavras.FoodOrder.util.DomainFixtures.someStreet2;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = CustomerOrderCreationController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class CustomerOrderCreationControllerWebMvcTest {

    @MockBean
    private final RestaurantService restaurantService;
    @MockBean
    private final OrderService orderService;
    @MockBean
    private final StreetService streetService;
    @MockBean
    private final MenuItemService menuItemService;
    @MockBean
    private final UtilityService utilityService;
    @MockBean
    private final RestaurantMapper restaurantMapper;
    @MockBean
    private final AddressMapper addressMapper;
    @MockBean
    private final OwnerMapper ownerMapper;
    @MockBean
    private final MenuItemOrderMapper menuItemOrderMapper;
    @MockBean
    private final MenuItemMapper menuItemMapper;
    @MockBean
    private final OrderMapper orderMapper;
    @MockBean
    private final AddressService addressService;
    @MockBean
    private final StreetMapper streetMapper;
    MockMvc mockMvc;

    @Test
    public void testShowAddressForm() throws Exception {
        //given
        List<Street> streets = List.of(
                someStreet1(), someStreet2());

        //when, then
        when(streetService.findAll()).thenReturn(streets);

        mockMvc.perform(get("/customer/address"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer-address-form"))
                .andExpect(model().attributeExists("addressDTO"));

        verify(streetService, times(1)).findAll();
    }


    @Test
    public void testAddMenuItemsNoItemsOrdered() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("addressDTO", new AddressDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/restaurants/addItems/{restaurantName}", "SampleRestaurant")
                        .flashAttr("menuItemOrdersDTO", new MenuItemOrdersDTO())
                        .session(session))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/customer/restaurants/{restaurantName}"))
                .andExpect(flash().attributeExists("noItemsOrdered"))
                .andExpect(flash().attribute("noItemsOrdered", "Your order does not contain any items, please try again."));

    }

}



