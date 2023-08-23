package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.entity.StreetEntity;

import java.util.Optional;

public interface StreetJpaRepository extends JpaRepository<StreetEntity, Integer> {

    Optional<StreetEntity> findByStreetName(String streetName);

    Optional<StreetEntity> findByStreetId(Integer streetId);
}
