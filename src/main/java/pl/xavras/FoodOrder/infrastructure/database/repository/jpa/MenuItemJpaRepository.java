package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;

public interface MenuItemJpaRepository extends JpaRepository<MenuItemEntity, Integer> {
}
