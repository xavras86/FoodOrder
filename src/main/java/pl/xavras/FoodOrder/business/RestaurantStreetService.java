package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.business.dao.RestaurantStreetDAO;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.Street;

@Service
@AllArgsConstructor
@Slf4j
public class RestaurantStreetService {

    private final RestaurantStreetDAO restaurantServiceDAO;


    @Transactional
    public Boolean isStreetInRestaurantRange(Restaurant restaurant, Street street) {
        return restaurantServiceDAO.isStreetInRestaurantRange(restaurant, street);

    }
}