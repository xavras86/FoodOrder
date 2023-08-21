package pl.xavras.FoodOrder.api.dto.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Restaurant;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class RestaurantMapperTest {

    private final RestaurantMapper restaurantMapper = Mappers.getMapper(RestaurantMapper.class);

    @Test
    public void testMapToDTO() {
        //given
        Address address = Address.builder()
                .country("Country")
                .city("City")
                .street("Street")
                .buildingNumber("123")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .name("Restaurant Name")
                .phone("123456789")
                .email("restaurant@example.com")
                .address(address)
                .build();


        //when
        RestaurantDTO restaurantDTO = restaurantMapper.map(restaurant);

        //then
        assertEquals(restaurant.getName(), restaurantDTO.getName());
        assertEquals(restaurant.getPhone(), restaurantDTO.getPhone());
        assertEquals(restaurant.getEmail(), restaurantDTO.getEmail());
        assertEquals(restaurant.getAddress().getCountry(), restaurantDTO.getCountry());
        assertEquals(restaurant.getAddress().getCity(), restaurantDTO.getCity());
        assertEquals(restaurant.getAddress().getStreet(), restaurantDTO.getStreet());
        assertEquals(restaurant.getAddress().getBuildingNumber(), restaurantDTO.getBuildingNumber());
    }

    @Test
    public void testMapToEntity() {
        //given
        RestaurantDTO restaurantDTO = RestaurantDTO.builder()
                .name("Restaurant Name")
                .phone("123456789")
                .email("restaurant@example.com")
                .country("Country")
                .city("City")
                .street("Street")
                .buildingNumber("123")
                .build();

        //when
        Restaurant restaurant = restaurantMapper.map(restaurantDTO);

        //then
        assertEquals(restaurantDTO.getName(), restaurant.getName());
        assertEquals(restaurantDTO.getPhone(), restaurant.getPhone());
        assertEquals(restaurantDTO.getEmail(), restaurant.getEmail());
        assertEquals(restaurantDTO.getCountry(), restaurant.getAddress().getCountry());
        assertEquals(restaurantDTO.getCity(), restaurant.getAddress().getCity());
        assertEquals(restaurantDTO.getStreet(), restaurant.getAddress().getStreet());
        assertEquals(restaurantDTO.getBuildingNumber(), restaurant.getAddress().getBuildingNumber());
    }
}


