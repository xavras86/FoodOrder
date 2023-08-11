package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.business.dao.RestaurantDAO;
import pl.xavras.FoodOrder.domain.*;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RestaurantService {

    private final RestaurantDAO restaurantDAO;


    public List<Restaurant> findAll() {
        return restaurantDAO.findAll();
    }

    public Set<Restaurant> findRestaurantsByStreetName(String streetName) {
        return restaurantDAO.findRestaurantsByStreetName(streetName);
    }

    public Restaurant findByName(String restaurantName) {
        return restaurantDAO.findByName(restaurantName)
                .orElseThrow(() -> new RuntimeException("Restaurant with name [%s] doest not exists"
                        .formatted(restaurantName)));
    }

    public Set<Restaurant> findByOwnerEmail(String ownerEmail) {
        return restaurantDAO.findRestaurantsByOwner(ownerEmail);
    }


    public Set<MenuItem> getAvailableMenuItems(Restaurant restaurant) {
        return restaurant.getMenuItems().stream()
                .filter(a -> a.getAvailable())
                .collect(Collectors.toSet());
    }

    @Transactional
    public Restaurant saveNewRestaurant(Restaurant newRestaurant, Address newAddress) {
        return restaurantDAO.createNewRestaurant(newRestaurant, newAddress);
    }

    @Transactional
    public void alternateCoverageStateForStreet(String restaurantName, Street street) {
       restaurantDAO.alternateCoverageStateForStreet(restaurantName, street);
    }

    public Boolean chceckStreetCoverageForRestaurant(String restaurantName, Street street) {
        Set<RestaurantStreet> restaurantStreets = findByName(restaurantName).getRestaurantStreets();
        return restaurantStreets.stream().anyMatch(a -> street.equals(a.getStreet()));
    }
}

