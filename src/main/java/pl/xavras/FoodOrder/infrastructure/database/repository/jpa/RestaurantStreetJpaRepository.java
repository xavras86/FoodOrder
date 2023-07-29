package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;

public interface RestaurantStreetJpaRepository extends JpaRepository<RestaurantStreetEntity, Integer> {
}
