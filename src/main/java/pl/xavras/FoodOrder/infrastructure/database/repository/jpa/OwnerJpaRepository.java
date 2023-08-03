package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.entity.CustomerEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OwnerEntity;

import java.util.Optional;

public interface OwnerJpaRepository extends JpaRepository<OwnerEntity, Integer> {

    Optional<OwnerEntity> findByEmail(String email);


}
