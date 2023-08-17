package pl.xavras.FoodOrder.util;

import lombok.experimental.UtilityClass;
import pl.xavras.FoodOrder.api.dto.*;

import java.util.List;


@UtilityClass
public class DtoFixtures {

    public static RestaurantsDTO someRestaurantsDTO(){
        return  RestaurantsDTO.builder()
                .restaurants(List.of(someRestaurantDTO1(), someRestaurantDTO2(), someRestaurantDTO3()))
                .build();
    }

    public static StreetsDTO someStreetsDTO(){
        return  StreetsDTO.builder()
                .streetDTOList(List.of(someStreetDTO1(), someStreetDTO2(), someStreetDTO3()))
                .build();
    }

    public static RestaurantDTO someRestaurantDTO1() {
        return RestaurantDTO.builder()
                .name("someName1")
                .phone("+48 123 123 123")
                .email("jakUmamy@test.pl")
                .country("Polska")
                .city("Poznań")
                .street("Polna")
                .buildingNumber("20")
                .build();
    }

    public static RestaurantDTO someRestaurantDTO2() {
        return RestaurantDTO.builder()
                .name("someName2")
                .phone("+48 123 432 123")
                .email("restautant2@test.pl")
                .country("Polska")
                .city("Poznań")
                .street("Pomorska")
                .buildingNumber("15")
                .build();
    }

    public static RestaurantDTO someRestaurantDTO3() {
        return RestaurantDTO.builder()
                .name("someName3")
                .phone("+48 123 123 321")
                .email("restautant3@test.pl")
                .country("Polska")
                .city("Poznań")
                .street("Wrocławska")
                .buildingNumber("12")
                .build();
    }


    public static AddressDTO someAddressDTO1() {
        return AddressDTO.builder()
                .country("Polska")
                .city("Poznań")
                .street("Polna")
                .buildingNumber("20")
                .build();
    }


    public static OwnerDTO someOwnerDTO1() {
        return OwnerDTO.builder()
                .name("Jan")
                .surname("Kowalski")
                .email("jan@kowalski.pl")
                .phone("+48 111 222 333")
                .build();
    }

    public static StreetDTO someStreetDTO1() {
        return StreetDTO.builder()
                .streetId(1)
                .city("Poznań")
                .streetName("Rolna")
                .build();
    }

    public static StreetDTO someStreetDTO2() {
        return StreetDTO.builder()
                .streetId(2)
                .city("Poznań")
                .streetName("Dolna")
                .build();
    }

    public static StreetDTO someStreetDTO3() {
        return StreetDTO.builder()
                .streetId(3)
                .city("Poznań")
                .streetName("Warszawska")
                .build();
    }
}
