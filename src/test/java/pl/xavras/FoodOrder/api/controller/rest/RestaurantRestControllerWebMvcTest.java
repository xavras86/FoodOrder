package pl.xavras.FoodOrder.api.controller.rest;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.api.dto.mapper.StreetMapper;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.business.StreetService;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.Street;
import pl.xavras.FoodOrder.util.DtoFixtures;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.xavras.FoodOrder.util.DomainFixtures.*;

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
        Restaurant restaurant = someRestaurant1().withName(restaurantName);
        RestaurantDTO restaurantDTO = DtoFixtures.someRestaurantDTO1().withName(restaurantName);

        Mockito.when(restaurantService.findByName(restaurantName)).thenReturn(restaurant);
        Mockito.when(restaurantMapper.map(ArgumentMatchers.any(Restaurant.class))).thenReturn(restaurantDTO);

        //when, then

        String endpoint = RestaurantRestController.RESTAURANT + "/" + restaurant.getName();

        mockMvc.perform(get("/api/restaurant/{restaurantName}", restaurantName))
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

    @Test
    public void testRestaurantsList() throws Exception {
        List<Restaurant> restaurants = List.of(
                someRestaurant1(),
                someRestaurant2()
        );

        when(restaurantService.findAll()).thenReturn(restaurants);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.restaurants.length()").value(2));
    }

    @Test
    public void testRestaurantDetails() throws Exception {
        Restaurant restaurant = someRestaurant1().withName("SomeRestaurant1");
        RestaurantDTO restaurantDTO = DtoFixtures.someRestaurantDTO1().withName(restaurant.getName());

        when(restaurantMapper.map(ArgumentMatchers.any(Restaurant.class))).thenReturn(restaurantDTO);
        when(restaurantService.findByName("SomeRestaurant1")).thenReturn(restaurant);

        mockMvc.perform(get("/api/restaurant/{restaurantName}", "SomeRestaurant1"))
                .andExpect(status().isOk());
        verify(restaurantService, times(1)).findByName("SomeRestaurant1");
    }


    @Test
    public void testDeliveryStreets() throws Exception {
        String restaurantName = "Some Restaurant1";
        Set<Street> streets = Set.of(someStreet1().withStreetName("someStreet1"), someStreet2().withStreetName("someStreet2"));


        when(restaurantService.findStreetsByRestaurantName(restaurantName)).thenReturn(streets);

        mockMvc.perform(get("/api/restaurant/streets/{restaurantName}", restaurantName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.streetDTOList", hasSize(2)));

        verify(restaurantService, times(1)).findStreetsByRestaurantName("Some Restaurant1");
    }


    @Test
    void thatRestaurantCanBeEditedCorrectly() throws Exception {
        //given
        String restaurantName = "Some name";
        Restaurant restaurant = someRestaurant1().withName(restaurantName);
        RestaurantDTO restaurantDTO = DtoFixtures.someRestaurantDTO1().withName(restaurantName);

        Mockito.when(restaurantService.findByName(restaurantName)).thenReturn(restaurant);
        Mockito.when(restaurantMapper.map(ArgumentMatchers.any(Restaurant.class))).thenReturn(restaurantDTO);

        //when, then

        String endpoint = RestaurantRestController.RESTAURANT + "/" + restaurant.getName();

        mockMvc.perform(get("/api/restaurant/{restaurantName}", restaurantName))
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


}

