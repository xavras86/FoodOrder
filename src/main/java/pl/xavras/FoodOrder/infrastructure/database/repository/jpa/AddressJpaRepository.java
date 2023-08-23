package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;

import java.util.Optional;

@Repository
public interface AddressJpaRepository extends JpaRepository<AddressEntity, Integer> {

    Optional<AddressEntity> findByCountryAndCityAndStreetAndBuildingNumber(String country, String city, String street, String buildingNumber);


}
