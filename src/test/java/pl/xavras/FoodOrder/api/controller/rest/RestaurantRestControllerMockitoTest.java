package pl.xavras.FoodOrder.api.controller.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantsDTO;
import pl.xavras.FoodOrder.api.dto.StreetsDTO;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.api.dto.mapper.StreetMapper;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.business.StreetService;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.Street;
import pl.xavras.FoodOrder.util.DomainFixtures;
import pl.xavras.FoodOrder.util.DtoFixtures;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RestaurantRestControllerMockitoTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private StreetService streetService;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private StreetMapper streetMapper;

    @InjectMocks
    private RestaurantRestController restaurantRestController;

    @Test
    void thatRetrievingRestaurantWorksCorrectly() {
        //given
        String restaurantName = "some_name1";
        Restaurant restaurant = DomainFixtures.someRestaurant1();

        Mockito.when(restaurantService.findByName(restaurantName)).thenReturn(restaurant);
        Mockito.when(restaurantMapper.map(restaurant)).thenReturn(DtoFixtures.someRestaurantDTO1());

        //when
        RestaurantDTO result = restaurantRestController.restaurantDetails(restaurantName);

        //then
        assertThat(result).isEqualTo(DtoFixtures.someRestaurantDTO1());
    }

    @Test
    void thatRetrievingRestaurantListWorksCorrectly() {
        //given
        Restaurant restaurant1 = DomainFixtures.someRestaurant1();
        Restaurant restaurant2 = DomainFixtures.someRestaurant2();
        Restaurant restaurant3 = DomainFixtures.someRestaurant3();

        Mockito.when(restaurantService.findAll()).thenReturn(List.of(restaurant1, restaurant2, restaurant3));
        Mockito.when(restaurantMapper.map(restaurant1)).thenReturn(DtoFixtures.someRestaurantDTO1());
        Mockito.when(restaurantMapper.map(restaurant2)).thenReturn(DtoFixtures.someRestaurantDTO2());
        Mockito.when(restaurantMapper.map(restaurant3)).thenReturn(DtoFixtures.someRestaurantDTO3());

        //when
        RestaurantsDTO result = restaurantRestController.restaurantsList();

        //then
        assertThat(result).isEqualTo(DtoFixtures.someRestaurantsDTO());
    }

    @Test
    void thatRetrievingStreetsListWorksCorrectly() {
        //given
        String restaurantName = "some_name1";
        Street street1 = DomainFixtures.someStreet1();
        Street street2 = DomainFixtures.someStreet2();
        Street street3 = DomainFixtures.someStreet3();

        Mockito.when(streetService.findStreetsByRestaurantName(restaurantName)).thenReturn(List.of(street1, street2, street3));
        Mockito.when(streetMapper.map(street1)).thenReturn(DtoFixtures.someStreetDTO1());
        Mockito.when(streetMapper.map(street2)).thenReturn(DtoFixtures.someStreetDTO2());
        Mockito.when(streetMapper.map(street3)).thenReturn(DtoFixtures.someStreetDTO3());

        //when
        StreetsDTO result = restaurantRestController.deliveryStreets(restaurantName);

        //then
        assertThat(result).isEqualTo(DtoFixtures.someStreetsDTO());
    }


}