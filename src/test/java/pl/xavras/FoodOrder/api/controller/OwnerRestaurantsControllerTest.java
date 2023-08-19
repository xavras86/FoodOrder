package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.mapper.MenuItemMapper;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.business.*;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.xavras.FoodOrder.api.controller.OwnerRestaurantsController.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = OwnerRestaurantsController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class OwnerRestaurantsControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private OwnerService ownerService;
    @MockBean
    private MenuItemService menuItemService;
    @MockBean
    private StreetService streetService;
    @MockBean
    private RestaurantMapper restaurantMapper;
    @MockBean
    private MenuItemMapper menuItemMapper;
    @MockBean
    private UtilityService utilityService;

    public static Stream<Arguments> thatPhoneValidationWorksCorrectlyAddRestaurant() {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(false, "+48 504 203 260@@"),
                Arguments.of(false, "+48.504.203.260"),
                Arguments.of(false, "+55(123) 456-78-90-"),
                Arguments.of(false, "+55(123) - 456-78-90"),
                Arguments.of(false, "504.203.260"),
                Arguments.of(false, " "),
                Arguments.of(false, "-"),
                Arguments.of(false, "()"),
                Arguments.of(false, "() + ()"),
                Arguments.of(false, "(21 7777"),
                Arguments.of(false, "+48 (21)"),
                Arguments.of(false, "+"),
                Arguments.of(false, " 1"),
                Arguments.of(false, "1"),
                Arguments.of(false, "555-5555-555"),
                Arguments.of(false, "4812504203260"),
                Arguments.of(false, "+48 (12) 504 203 260"),
                Arguments.of(false, "+48 (12) 504-203-260"),
                Arguments.of(false, "+48(12)504203260"),
                Arguments.of(false, "+4812504203260"),
                Arguments.of(true, "+48 504 203 260")
        );
    }

    @ParameterizedTest
    @MethodSource
    void thatPhoneValidationWorksCorrectlyAddRestaurant(Boolean correctPhone, String phone) throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = RestaurantDTO.builder().phone(phone).build().asMap();
        parametersMap.forEach(parameters::add);

        //when, then
        if (correctPhone) {
            mockMvc.perform(post(RESTAURANT_OWNER_ADD_RESTAURANT).params(parameters))
                    .andExpect(status().isFound())
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(view().name("redirect:/owner/restaurants"));
        } else {
            mockMvc.perform(post(RESTAURANT_OWNER_ADD_RESTAURANT).params(parameters))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(phone)))
                    .andExpect(view().name("error"));
        }
    }




}