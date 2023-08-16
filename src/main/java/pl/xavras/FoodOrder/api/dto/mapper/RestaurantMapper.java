package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Restaurant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {




    default RestaurantDTO map(Restaurant restaurant){
        return RestaurantDTO.builder()
                .name(restaurant.getName())
                .phone(restaurant.getPhone())
                .email(restaurant.getEmail())
                .country(restaurant.getAddress().getCountry())
                .city(restaurant.getAddress().getCity())
                .street(restaurant.getAddress().getStreet())
                .buildingNumber(restaurant.getAddress().getBuildingNumber())
                .build();
    }

    default Restaurant map(RestaurantDTO restaurantDTO) {
        return Restaurant.builder()
                .name(restaurantDTO.getName())
                .phone(restaurantDTO.getPhone())
                .email(restaurantDTO.getEmail())
                .address(Address.builder()
                        .country(restaurantDTO.getCountry())
                        .city(restaurantDTO.getCity())
                        .street(restaurantDTO.getStreet())
                        .buildingNumber(restaurantDTO.getBuildingNumber())
                        .build())
                .build();
    }



}
