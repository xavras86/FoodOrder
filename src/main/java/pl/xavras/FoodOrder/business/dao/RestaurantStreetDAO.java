package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.Street;

public interface RestaurantStreetDAO {

    Boolean isStreetInRestaurantRange(Restaurant restaurant, Street street);

}
