package pl.xavras.FoodOrder.business.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.Street;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RestaurantDAO {

    Optional<Restaurant> findByName(String name);

    List<Restaurant> findAll();

    Set<Restaurant> findRestaurantsByStreetName(String streetName);


    Set<Restaurant> findRestaurantsByOwner(String ownerEmail);

    Restaurant createNewRestaurant(Restaurant newRestaurant, Address newAddress);

    void alternateCoverageStateForStreet(String restaurantName, Street street);

    Page<Restaurant> findAll(Pageable pageable);

    Page<Restaurant> findByOwner(Pageable pageable, Owner activeOwner);


    Page<Restaurant> findRestaurantsByStreetNamePaged(String streetName, Pageable pageable);
}
