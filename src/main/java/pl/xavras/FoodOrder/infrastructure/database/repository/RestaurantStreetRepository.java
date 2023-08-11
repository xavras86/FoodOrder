package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.RestaurantDAO;
import pl.xavras.FoodOrder.business.dao.RestaurantStreetDAO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.RestaurantStreet;
import pl.xavras.FoodOrder.domain.Street;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.AddressEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.RestaurantStreetEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RestaurantStreetRepository implements RestaurantStreetDAO {

    RestaurantEntityMapper restaurantEntityMapper;
    RestaurantStreetEntityMapper restaurantStreetEntityMapper;

    @Override
    public Boolean isStreetInRestaurantRange(Restaurant restaurant, Street street) {

        Set<RestaurantStreet> restaurantStreets = restaurant.getRestaurantStreets();
        Set<RestaurantStreetEntity> restaurantStreetEntities = restaurantStreetEntityMapper.mapToEntity(restaurantStreets);
        RestaurantEntity restaurantEntity = restaurantEntityMapper.mapToEntity(restaurant);
        RestaurantStreet restaurantStreet = RestaurantStreet.builder()
                .street(street)
                .restaurant(restaurant)
                .build();

        if(restaurantStreets.contains(restaurantStreet)){
            restaurantStreets.remove(restaurantStreet);
            restaurantEntity.setRestaurantStreets(restaurantStreetEntities);
            return false;
        }else {
            restaurantStreets.add(restaurantStreet);
            restaurantEntity.setRestaurantStreets(restaurantStreetEntities);
            return true;
        }
    }
}

