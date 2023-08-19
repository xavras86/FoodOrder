package pl.xavras.FoodOrder.api.controller.rest;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.api.dto.mapper.StreetMapper;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.business.StreetService;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.util.DomainFixtures;
import pl.xavras.FoodOrder.util.DtoFixtures;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RestaurantRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestaurantRestControllerWebMvcTest {


    private final MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private StreetService streetService;

    @MockBean
    private StreetMapper streetMapper;

    @MockBean
    private RestaurantMapper restaurantMapper;

    @Test
    void thatRestaurantCanBeRetrieverCorrectly() throws Exception {
        //given
        String restaurantName = "Some name";
        Restaurant restaurant = DomainFixtures.someRestaurant1().withName(restaurantName);
        RestaurantDTO restaurantDTO = DtoFixtures.someRestaurantDTO1().withName(restaurantName);

        Mockito.when(restaurantService.findByName(restaurantName)).thenReturn(restaurant);
        Mockito.when(restaurantMapper.map(ArgumentMatchers.any(Restaurant.class))).thenReturn(restaurantDTO);

        //when, then

        String endpoint = RestaurantRestController.RESTAURANT + "/" + restaurant.getName();


        mockMvc.perform(get(endpoint, restaurantName))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(restaurantDTO.getName())))
                .andExpect(jsonPath("$.phone", is(restaurantDTO.getPhone())))
                .andExpect(jsonPath("$.email", is(restaurantDTO.getEmail())))
                .andExpect(jsonPath("$.country", is(restaurantDTO.getCountry())))
                .andExpect(jsonPath("$.city", is(restaurantDTO.getCity())))
                .andExpect(jsonPath("$.street", is(restaurantDTO.getStreet())))
                .andExpect(jsonPath("$.buildingNumber", is(restaurantDTO.getBuildingNumber())));
    }

    public static Stream<Arguments> thatPhoneValidationWorksCorrectly() {
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

    @Test
    void thatEmailValidationWorksCorrectly() throws Exception {
        //given
        final var request = """
                {
                    "email": "badEmail"
                }
                """;
        //when, then
        mockMvc.perform(
                        post(RestaurantRestController.RESTAURANT_ADD)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorId", Matchers.notNullValue()));
    }

    @ParameterizedTest
    @MethodSource
    void thatPhoneValidationWorksCorrectly(Boolean correctPhone, String phone) throws Exception {
        //given
        final var request = """
                {
                    "phone": "%s"
                }
                """.formatted(phone);


        when(restaurantService.saveNewRestaurant(any(Restaurant.class)))
                .thenReturn(DomainFixtures.someRestaurant1().withName("SomeName1"));

        //when, then
        if (correctPhone) {
            String expectedRedirect
                    = RestaurantRestController.RESTAURANT_ADD + "/SomeName1";
            mockMvc.perform(post(RestaurantRestController.RESTAURANT_ADD)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirect));
        } else {
            mockMvc.perform(post(RestaurantRestController.RESTAURANT_ADD)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorId", Matchers.notNullValue()));
        }

    }


}

