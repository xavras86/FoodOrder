package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemOrderEntity;

public interface MenuItemOrdersJpaRepository extends JpaRepository<MenuItemOrderEntity, Integer> {
}
