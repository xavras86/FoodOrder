package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.RestaurantStreetDAO;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantStreetJpaRepository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RestaurantStreetRepository implements RestaurantStreetDAO {

    private final RestaurantStreetJpaRepository restaurantStreetJpaRepository;



    public void delete(RestaurantStreetEntity restaurantStreetEntity) {
        restaurantStreetJpaRepository.delete(restaurantStreetEntity);
        log.info("USUWAM RSE!");
    }
}

