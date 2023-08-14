package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;

import java.util.Optional;

public interface MenuItemJpaRepository extends JpaRepository<MenuItemEntity, Integer> {


    Optional<MenuItemEntity> findByName(String name);

    Page<MenuItemEntity> findByRestaurant(RestaurantEntity restaurant, Pageable pageable);

    Page<MenuItemEntity> findByRestaurantAndAvailable(RestaurantEntity restaurantEntity, boolean available, Pageable pageable);
}
