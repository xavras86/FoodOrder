package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RestaurantDAO {

    Optional<Restaurant> findByName(String name);

    List<Restaurant> findAll();

    Set<Restaurant> findRestaurantsByStreetName(String streetName);


    Set<Restaurant> findRestaurantsByOwner(String ownerEmail);
}
