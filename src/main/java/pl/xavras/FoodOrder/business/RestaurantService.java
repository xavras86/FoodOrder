package pl.xavras.FoodOrder.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.business.dao.RestaurantDAO;
import pl.xavras.FoodOrder.domain.*;

import java.util.List;
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

    public Page<Restaurant> findRestaurantsByStreetNamePaged(String streetName, Pageable pageable) {
        return restaurantDAO.findRestaurantsByStreetNamePaged(streetName, pageable);
    }


    public Restaurant findByName(String restaurantName) {
        return restaurantDAO.findByName(restaurantName)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with name [%s] doest not exists"
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

    public Boolean checkStreetCoverageForRestaurant(String restaurantName, Street street) {
        Set<RestaurantStreet> restaurantStreets = findByName(restaurantName).getRestaurantStreets();
        return restaurantStreets.stream().anyMatch(a -> street.equals(a.getStreet()));
    }

    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantDAO.findAll(pageable);
    }

    public Page<Restaurant> findByOwner(Pageable pageable, Owner activeOwner) {
        return restaurantDAO.findByOwner(pageable, activeOwner);
    }


}

