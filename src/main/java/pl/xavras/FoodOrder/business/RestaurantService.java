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
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;

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

    public Page<Restaurant> findRestaurantsByStreetNamePaged(String streetName, Pageable pageable) {
        return restaurantDAO.findRestaurantsByStreetNamePaged(streetName, pageable);
    }

    public Restaurant findByName(String restaurantName) {
        return restaurantDAO.findByName(restaurantName)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with name [%s] doest not exists"
                        .formatted(restaurantName)));
    }

    @Transactional
    public Restaurant saveNewRestaurant(Restaurant newRestaurant) {
        return restaurantDAO.createNewRestaurant(newRestaurant);
    }

    @Transactional
    public void alternateCoverageStateForStreet(String restaurantName, Street street) {
        restaurantDAO.alternateCoverageStateForStreet(restaurantName, street);
    }



    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantDAO.findAll(pageable);
    }

    public Page<Restaurant> findByOwner(Pageable pageable, Owner activeOwner) {
        return restaurantDAO.findByOwner(pageable, activeOwner);
    }

    @Transactional
    public Restaurant editRestaurant(String currentName, String name, String phone, String email) {
        return restaurantDAO.editRestaurant(currentName, name, phone, email);

    }

    public Set<Street> findStreetsByRestaurantName(String restaurantName) {
        return restaurantDAO.findStreetsByRestaurantName(restaurantName);
    }
}

