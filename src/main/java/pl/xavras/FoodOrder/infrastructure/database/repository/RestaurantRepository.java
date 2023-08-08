package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.RestaurantDAO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.RestaurantStreet;
import pl.xavras.FoodOrder.infrastructure.database.entity.OwnerEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.OwnerEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.RestaurantEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class RestaurantRepository implements RestaurantDAO {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final StreetRepository streetRepository;


    @Override
    public Optional<Restaurant> findByName(String name) {
        return restaurantJpaRepository.findByName(name)
                .map(restaurantEntityMapper::mapFromEntity);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantJpaRepository.findAll().stream()
                .map(restaurantEntityMapper::mapFromEntity).toList();
    }

    @Override
    public Set<Restaurant> findRestaurantsByStreetName(String streetName) {
        var street = streetRepository.findByStreet(streetName)
                .orElseThrow(() -> new RuntimeException("wrong street name [%s]".formatted(streetName)));
        return street.getRestaurantStreets().stream().map(RestaurantStreet::getRestaurant)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Restaurant> findRestaurantsByOwner(String ownerEmail) {

        return restaurantJpaRepository.findRestaurantsByOwner(ownerEmail)
                .stream().map(restaurantEntityMapper::mapFromEntity).collect(Collectors.toSet());
    };



}
